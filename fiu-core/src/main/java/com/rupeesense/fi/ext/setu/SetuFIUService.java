package com.rupeesense.fi.ext.setu;

import static com.rupeesense.fi.CoreModule.ONE_MONEY_CLIENT_NAME;
import static com.rupeesense.fi.CoreModule.SETU_CLIENT_NAME;

import com.rupeesense.fi.aa.exception.AAClientException;
import com.rupeesense.fi.aa.exception.AAServerException;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentInitiateAPIResponse;
import com.rupeesense.fi.ext.setu.request.SetuConsentAPIRequest;
import com.rupeesense.fi.ext.setu.request.SetuDataRequest;
import com.rupeesense.fi.ext.setu.response.SetuConsentInitiateResponse;
import com.rupeesense.fi.ext.setu.response.SetuSessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class SetuFIUService {


  private final WebClient webClient;

  @Autowired
  public SetuFIUService(@Name(SETU_CLIENT_NAME) WebClient webClient) {
    this.webClient = webClient;
  }

  public SetuConsentInitiateResponse initiateConsent(SetuConsentAPIRequest request) {
    return webClient.post()
        .uri("/consents")
        .bodyValue(request)
        .retrieve()
        .onStatus(HttpStatus::isError, this::createException)
        .bodyToMono(SetuConsentInitiateResponse.class)
        .block();
  }

  public SetuSessionResponse createDataRequest(SetuDataRequest request) {
    return webClient.post()
        .uri("/sessions")
        .bodyValue(request)
        .retrieve()
        .onStatus(HttpStatus::isError, this::createException)
        .bodyToMono(SetuSessionResponse.class)
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
