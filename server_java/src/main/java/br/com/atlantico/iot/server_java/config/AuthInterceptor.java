package br.com.atlantico.iot.server_java.config;

import br.com.atlantico.iot.server_java.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.apache.http.HttpStatus.SC_OK;

/**
 * Created by everton on 26/06/17.
 */
@Component("AuthInterceptor")
@PropertySource("classpath:application.properties")
public class AuthInterceptor implements HandlerInterceptor {

	@Value("${platform.url}")
	private String platformUrl;

	@Value("${security.username}")
	private String username;

	@Value("${security.passwd}")
	private String passwd;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		if (user == null) {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			try {
				HttpPost httpPost = new HttpPost(platformUrl + "/auth");
				httpPost.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());

				user = new User();
				user.setUsername(username);
				user.setPasswd(passwd);
				ObjectMapper objectMapper = new ObjectMapper();
				StringEntity entity = new StringEntity(objectMapper.writeValueAsString(user),
						ContentType.APPLICATION_JSON);

				httpPost.setEntity(entity);
				CloseableHttpResponse authResponse = httpclient.execute(httpPost);
				try {
					if (SC_OK == authResponse.getStatusLine().getStatusCode()) {
						User newUser = objectMapper.readValue(EntityUtils.toString(authResponse.getEntity()),
								User.class);
						user.setJwt(newUser.getJwt());
						session.setAttribute("user", user);
					} else {
						throw new Exception("Failed to get jwt");
					}
				} finally {
					authResponse.close();
				}
			} finally {
				httpclient.close();
			}
			return true;
		} else {
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
}
