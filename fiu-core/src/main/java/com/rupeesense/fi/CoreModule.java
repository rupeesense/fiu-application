package com.rupeesense.fi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CoreModule {

  public static final String ONE_MONEY_CLIENT_NAME = "one-money-web-client";

  public static final String SETU_CLIENT_NAME = "setu-web-client";

  @Bean(name = SETU_CLIENT_NAME)
  public WebClient getSetuClient() {
    return WebClient
        .builder()
        .baseUrl("https://fiu-uat.setu.co")
        .defaultHeader("Content-Type", "application/json")
        .defaultHeader("x-client-id", "f26b25b8-8e33-4c86-a28e-4575ddeeb09a")
        .defaultHeader("x-client-secret", "02325c82-505e-4b62-9135-0325f25f4dfd")
        .build();
  }

  @Bean
  public ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper =  new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }
}
