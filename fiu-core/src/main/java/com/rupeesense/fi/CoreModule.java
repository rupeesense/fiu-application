package com.rupeesense.fi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.zalando.logbook.netty.LogbookClientHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class CoreModule {

  public static final String SETU_CLIENT_NAME = "setu-web-client";

  @Autowired
  @Bean(name = SETU_CLIENT_NAME)
  public WebClient getSetuClient(FIUServiceConfig fiuServiceConfig,
      LogbookClientHandler logbookClientHandler) {
    HttpClient client = HttpClient.create()
        .doOnConnected(
            (connection -> connection.addHandlerLast(logbookClientHandler))
        )
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
        .responseTimeout(Duration.ofSeconds(12));

    System.out.println(fiuServiceConfig.getSetuClientSecret());
    return WebClient
        .builder()
        .clientConnector(new ReactorClientHttpConnector(client))
        .baseUrl(fiuServiceConfig.getSetuURI())
        .defaultHeader("Content-Type", "application/json")
        .defaultHeader("x-client-id", fiuServiceConfig.getSetuClientId())
        .defaultHeader("x-client-secret", fiuServiceConfig.getSetuClientSecret())
        .build();
  }

  @Bean
  public ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }
}
