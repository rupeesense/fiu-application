package com.rupeesense.fi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class CoreModule {

  public static final String SETU_CLIENT_NAME = "setu-web-client";

  @Bean(name = SETU_CLIENT_NAME)
  public WebClient getSetuClient() {
    HttpClient client = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
        .responseTimeout(Duration.ofSeconds(12));

    return WebClient
        .builder()
        .clientConnector(new ReactorClientHttpConnector(client))
        .baseUrl("https://fiu-uat.setu.co")
        .defaultHeader("Content-Type", "application/json")
        .defaultHeader("x-client-id", "f26b25b8-8e33-4c86-a28e-4575ddeeb09a")
        .defaultHeader("x-client-secret", "02325c82-505e-4b62-9135-0325f25f4dfd")
        .build();
  }

  @Bean
  public ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }
}
