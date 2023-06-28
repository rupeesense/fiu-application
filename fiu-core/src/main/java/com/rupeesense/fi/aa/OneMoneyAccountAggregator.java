package com.rupeesense.fi.aa;

import static com.rupeesense.fi.CoreModule.ONE_MONEY_CLIENT_NAME;

import com.rupeesense.fi.api.request.onemoney.ConsentAPIRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OneMoneyAccountAggregator {

  private final WebClient webClient;

  @Autowired
  public OneMoneyAccountAggregator(@Name(ONE_MONEY_CLIENT_NAME) WebClient webClient) {
    this.webClient = webClient;
  }

  public String initiateConsent(ConsentAPIRequest request) {
    return webClient.post()
        .uri("/aa/consent")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }
}
