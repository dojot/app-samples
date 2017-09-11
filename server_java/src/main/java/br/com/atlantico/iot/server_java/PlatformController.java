package br.com.atlantico.iot.server_java;

import br.com.atlantico.iot.server_java.domain.AttributeResponseDTO;
import br.com.atlantico.iot.server_java.domain.DeviceDTO;
import br.com.atlantico.iot.server_java.domain.Message;
import br.com.atlantico.iot.server_java.domain.User;
import br.com.atlantico.iot.server_java.domain.platform.ContextElement;
import br.com.atlantico.iot.server_java.domain.platform.Entity;
import br.com.atlantico.iot.server_java.domain.platform.EntityRequestDTO;
import br.com.atlantico.iot.server_java.domain.platform.EntityResponseDTO;
import br.com.atlantico.iot.server_java.domain.platform.NotifyConditions;
import br.com.atlantico.iot.server_java.domain.platform.UnsubscribeRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.apache.http.HttpStatus.SC_OK;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by everton on 12/06/17.
 */
@RestController
public class PlatformController {

	@Value("${platform.url}")
	private String platformUrl;

	@Value("${fiware-service}")
	private String fiwareService;

	@Value("${fiware-servicepath}")
	private String fiwareServicePath;

	@Value("${server.port}")
	private String serverPort;
	
	@Autowired
	private SocketIOServer socketIOServer;

	@RequestMapping(path = { "/device", "/device/*" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String entities(HttpServletRequest request) throws IOException {
		String url = getUrlOrion(request);
		return doGet(request, url);
	}

	@RequestMapping(path = "/sth/**", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String sth(HttpServletRequest request) throws IOException {
		StringBuilder url = new StringBuilder();
		url.append(getUrlSth(request)).append("?").append(request.getQueryString());
		return doGet(request, url.toString());
	}

	@RequestMapping(path = "/notifications", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody ResponseEntity<String> notifications(@RequestBody EntityResponseDTO entityResponseDTO, HttpServletResponse response) throws JsonProcessingException {
		ContextElement contextElement = entityResponseDTO.getContextResponses().get(0).getContextElement();
		String topic = "/device/"+contextElement.getId(); 
		AttributeResponseDTO attribute = contextElement.getAttributes().stream().map(attr -> {
				return new AttributeResponseDTO(attr.getName(),attr.getValue());				
			}).collect(Collectors.toList()).get(0);		
			ObjectMapper objectMapper = new ObjectMapper();
			String attributeString = objectMapper.writeValueAsString(attribute);
		 socketIOServer.getBroadcastOperations().sendEvent(topic, new Message(attributeString));
		 return new ResponseEntity<String>(HttpStatus.OK);		
	}

	@RequestMapping(path = "/subscribe", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String subscribe(HttpServletRequest request, @RequestBody DeviceDTO deviceDto) {

		Entity entity = new Entity();
		entity.setId(deviceDto.getId());

		EntityRequestDTO entityRequestDTO = new EntityRequestDTO();
		entityRequestDTO.addEntity(entity);	
		entityRequestDTO.setAttributes(deviceDto.getAttributes());
		StringBuilder builder = new StringBuilder();
		builder.append("http://").append(getIp()).append(":"+serverPort).append("/notifications");
		entityRequestDTO.setReference(builder.toString());
		NotifyConditions notifyConditions = new NotifyConditions();
		notifyConditions.setCondValues(deviceDto.getAttributes());
		entityRequestDTO.addNotifyCondition(notifyConditions);
				
		ObjectMapper objectMapper = new ObjectMapper();
		String responseString = null;
		try {
			
			String requestString = objectMapper.writeValueAsString(entityRequestDTO);
			responseString = doPost(request, platformUrl + "/metric/v1/subscribeContext", requestString);
		} catch (IOException e1) {			
			e1.printStackTrace();
		}
		return responseString;
	}
	
	@RequestMapping(path= "/unsubscribe/{subscriptionId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String unsubscribe(HttpServletRequest request, @PathVariable String subscriptionId) {
			
		UnsubscribeRequest unsubscribe = new UnsubscribeRequest(subscriptionId);		
		ObjectMapper objectMapper = new ObjectMapper();		
		String responseString = null;		
		try {			
			String requestString = objectMapper.writeValueAsString(unsubscribe);
			responseString = doPost(request, platformUrl + "/metric/v1/unsubscribeContext", requestString);
		} catch (IOException e1) {			
			e1.printStackTrace();
		}
		
		return responseString;
	}

	private String doPost(HttpServletRequest request, String url, String requestString) throws IOException {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		addHeaders(request, httpPost);
		
		String responseString = null;		
		try {
			StringEntity stringEntity = new StringEntity(requestString, ContentType.APPLICATION_JSON);
			httpPost.setEntity(stringEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);			
            if(SC_OK == response.getStatusLine().getStatusCode()){
            	responseString = EntityUtils.toString(response.getEntity());            	
            }			
		} catch (IOException e1) {			
			e1.printStackTrace();
		}finally {
			httpClient.close();
		}
		return responseString;
	}
	
	private String doGet(HttpServletRequest request, String url) throws IOException {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		addHeaders(request, httpGet);
		
		String responseString = null;		
		try {		
			
			CloseableHttpResponse response = httpClient.execute(httpGet);			
            if(SC_OK == response.getStatusLine().getStatusCode()){
            	responseString = EntityUtils.toString(response.getEntity());            	
            }			
		} catch (IOException e1) {			
			e1.printStackTrace();
		}finally {
			httpClient.close();
		}
		return responseString;
	}
	
	private void addHeaders(HttpServletRequest request, HttpRequestBase httpRequestBase) {		
		httpRequestBase.addHeader("fiware-service", fiwareService);
		httpRequestBase.addHeader("fiware-servicepath", fiwareServicePath);
		
		HttpSession httpSession = request.getSession();
		User user = (User) httpSession.getAttribute("user");
		httpRequestBase.addHeader("authorization", "Bearer " + user.getJwt());
	}
	
	private String getUrlSth(HttpServletRequest request) {
		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String originalUrl = restOfTheUrl.replaceAll("/sth", "");
		StringBuilder sb = new StringBuilder();
		return sb.append(platformUrl+"/history/STH/v1/contextEntities").append(originalUrl).toString();
	}

	private String getUrlOrion(HttpServletRequest request) {
		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String originalUrl = restOfTheUrl.replaceAll("/device", "");
		StringBuilder sb = new StringBuilder();
		return sb.append(platformUrl+"/device").append(originalUrl).toString();
	}

	private String getIp() {
		String ip = "";
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				if (iface.isLoopback() || !iface.isUp()) {
					continue;
				}

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					ip = addr.getHostAddress();
					if (ip.startsWith("192")) {
						return ip;
					}
				}
			}
		} catch (Exception e) {
		}
		return ip;

	}

}
