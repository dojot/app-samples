package br.com.atlantico.iot.server_java.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PreDestroy;

/**
 * Created by everton on 13/06/17.
 */
@org.springframework.context.annotation.Configuration
@ComponentScan("br.com.atlantico.iot")
@PropertySource("classpath:application.properties")
public class WebSocketServer {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    @Value("${wss.server.host}")
    private String host;

    @Value("${wss.server.port}")
    private Integer port;

    private SocketIOServer socketIOServer;

    @Bean(name = "webSocketIOServer")
    public SocketIOServer webSocketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);	
        config.setPort(port);
        socketIOServer = new SocketIOServer(config);
        socketIOServer.addConnectListener(client -> {
            LOG.info("user connected");
        });
        socketIOServer.addDisconnectListener(client -> {
            LOG.info("user disconnected");
        });
        socketIOServer.start();
        LOG.info("Starting websocket server");
        return socketIOServer;
    }

    @PreDestroy
    public void stop() {
        LOG.info("Stop websocket server");
        socketIOServer.stop();
    }

}
