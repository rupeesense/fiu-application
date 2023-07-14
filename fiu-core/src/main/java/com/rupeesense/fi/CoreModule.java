package com.rupeesense.fi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CoreModule {

  public static final String ONE_MONEY_CLIENT_NAME = "one-money-web-client";

  public static final String SETU_CLIENT_NAME = "setu-web-client";

  //TODO: make this config driven
  @Bean(name = ONE_MONEY_CLIENT_NAME)
  public WebClient getOneMoneyWebClient() {
    return WebClient
        .builder()
        .baseUrl("https://api-sandbox.onemoney.in")
        .defaultHeader("Content-Type", "application/json")
        .defaultHeader("client_api_key", System.getenv("CLIENT_API_KEY"))
        .build();
  }

  @Bean
  public ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper =  new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }
}
