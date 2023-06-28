package com.rupeesense.fi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CoreModule {

  public static final String ONE_MONEY_CLIENT_NAME = "one-money-web-client";

  //TODO: make this config driven
  @Bean(name = ONE_MONEY_CLIENT_NAME)
  public WebClient getOneMoneyWebClient() {
    return WebClient
        .builder()
        .baseUrl("https://api-sandbox.onemoney.in")
        .defaultHeader("client_api_key", "a443370460ab1ed74ce411a1e38e1c5201a3a6c4a3be1a36d86558a0a6fda48261d02ab9")
        .build();
  }

}
