package com.rupeesense.fi.aa;

import static com.rupeesense.fi.CoreModule.ONE_MONEY_CLIENT_NAME;

import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentAPIResponse;
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

  //TODO: add error handling
  public OneMoneyConsentAPIResponse initiateConsent(OneMoneyConsentAPIRequest request) {
    return webClient.post()
        .uri("/aa/consent")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(OneMoneyConsentAPIResponse.class)
        .block();
  }
}
