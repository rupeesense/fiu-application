package com.rupeesense.fi.aa;

import static com.rupeesense.fi.CoreModule.ONE_MONEY_CLIENT_NAME;

import com.rupeesense.fi.aa.exception.AAClientException;
import com.rupeesense.fi.aa.exception.AAServerException;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentArtifactAPIResponse;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentInitiateAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OneMoneyAccountAggregator {

  private final WebClient webClient;

  @Autowired
  public OneMoneyAccountAggregator(@Name(ONE_MONEY_CLIENT_NAME) WebClient webClient) {
    this.webClient = webClient;
  }

  public OneMoneyConsentInitiateAPIResponse initiateConsent(OneMoneyConsentAPIRequest request) {
    return webClient.post()
        .uri("/aa/Consent")
        .bodyValue(request)
        .retrieve()
        .onStatus(HttpStatus::isError, this::createException)
        .bodyToMono(OneMoneyConsentInitiateAPIResponse.class)
        .block();
  }

  public OneMoneyConsentArtifactAPIResponse getConsentArtifact(String consentId) {
    return webClient.get()
        .uri("/aa/Consent/{consentId}", consentId)
        .retrieve()
        .onStatus(HttpStatus::isError, this::createException)
        .bodyToMono(OneMoneyConsentArtifactAPIResponse.class)
        .block();
  }

  public Mono<Throwable> createException(ClientResponse clientResponse) {
    return clientResponse.bodyToMono(String.class)
        .flatMap(errorMessage -> {
          if (clientResponse.statusCode().is4xxClientError()) {
            return Mono.error(new AAClientException(errorMessage));
          } else if (clientResponse.statusCode().is5xxServerError()) {
            return Mono.error(new AAServerException(errorMessage));
          } else {
            return Mono.error(new RuntimeException("Unexpected status code: " +
                clientResponse.statusCode() + " AA: " + this.getClass().getName()));
          }
        });
  }
}
