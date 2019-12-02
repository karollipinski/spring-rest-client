package net.atos.springrestclient.patient.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;

@Configuration
public class WebClientConfig {

    @Bean("serverWebClient")
    WebClient.Builder serverWebClient(){

        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(HttpClient.create()
                                                                                     .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)));
        return WebClient.builder()
                                       .clientConnector(httpConnector)
                                       .baseUrl("http://localhost:8090")
                                       .defaultHeader("Authorization", "Basic " + Base64Utils.encodeToString("admin:abc123".getBytes(StandardCharsets.UTF_8)));
    }


}
