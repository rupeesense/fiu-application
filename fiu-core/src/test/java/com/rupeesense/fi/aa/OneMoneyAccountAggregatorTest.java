package com.rupeesense.fi.aa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.ext.onemoney.request.FIDataRequest;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentArtifactAPIResponse;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentInitiateAPIResponse;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyRequestDataAPIResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

public class OneMoneyAccountAggregatorTest {

  private MockWebServer mockWebServer;
  //private OneMoneyAccountAggregator oneMoneyAccountAggregator;

  @BeforeEach
  public void setup() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    WebClient webClient = WebClient.builder()
        .baseUrl(mockWebServer.url("/").toString())
        .build();

    //oneMoneyAccountAggregator = new OneMoneyAccountAggregator(webClient);
  }

  @AfterEach
  public void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

//  @Test
//  public void testInitiateConsent_Success() throws InterruptedException {
//    // Prepare the request
//    OneMoneyConsentAPIRequest request = new OneMoneyConsentAPIRequest();
//    // Set up request with required data
//
//    // Prepare the response
//    OneMoneyConsentInitiateAPIResponse expectedResponse = new OneMoneyConsentInitiateAPIResponse();
//    // Set up expected response
//
//    // Enqueue a mock response
//    mockWebServer.enqueue(new MockResponse()
//        .setHeader("Content-Type", "application/json")
//        .setResponseCode(HttpStatus.OK.value())
//        .setBody(asJsonString(expectedResponse)));
//
//    // Perform the request
//    OneMoneyConsentInitiateAPIResponse responseMono = oneMoneyAccountAggregator.initiateConsent(request);
//
//    // Verify the request
//    RecordedRequest recordedRequest = mockWebServer.takeRequest();
//    assertThat(recordedRequest.getMethod()).isEqualTo("POST");
//    assertThat(recordedRequest.getPath()).isEqualTo("/aa/Consent");
//  }
//
//  @Test
//  public void testGetConsentArtifact_Success() throws InterruptedException {
//    // Prepare the response
//    OneMoneyConsentArtifactAPIResponse expectedResponse = new OneMoneyConsentArtifactAPIResponse();
//    // Set up expected response
//
//    // Enqueue a mock response
//    mockWebServer.enqueue(new MockResponse()
//        .setHeader("Content-Type", "application/json")
//        .setResponseCode(HttpStatus.OK.value())
//        .setBody(asJsonString(expectedResponse)));
//
//    // Perform the request
//    OneMoneyConsentArtifactAPIResponse responseMono = oneMoneyAccountAggregator.getConsentArtifact("123");
//
//    // Verify the request
//    RecordedRequest recordedRequest = mockWebServer.takeRequest();
//    assertThat(recordedRequest.getMethod()).isEqualTo("GET");
//    assertThat(recordedRequest.getPath()).isEqualTo("/aa/Consent/123");
//  }
//
//  @Test
//  public void testPlaceDataRequest_Success() throws InterruptedException {
//    // Prepare the request
//    FIDataRequest dataRequest = new FIDataRequest();
//    // Set up dataRequest with required data
//
//    // Prepare the response
//    OneMoneyRequestDataAPIResponse expectedResponse = new OneMoneyRequestDataAPIResponse();
//    // Set up expected response
//
//    // Enqueue a mock response
//    mockWebServer.enqueue(new MockResponse()
//        .setHeader("Content-Type", "application/json")
//        .setResponseCode(HttpStatus.OK.value())
//        .setBody(asJsonString(expectedResponse)));
//
//    // Perform the request
//    OneMoneyRequestDataAPIResponse responseMono = oneMoneyAccountAggregator.placeDataRequest(dataRequest);
//
//    // Verify the request
//    RecordedRequest recordedRequest = mockWebServer.takeRequest();
//    assertThat(recordedRequest.getMethod()).isEqualTo("POST");
//    assertThat(recordedRequest.getPath()).isEqualTo("/aa/FI/request");
//  }
//
//  @Test
//  public void testInitiateConsent_InvalidResponse() {
//    // Prepare the request
//    OneMoneyConsentAPIRequest request = new OneMoneyConsentAPIRequest();
//    // Set up request with required data
//
//    // Enqueue an invalid response
//    mockWebServer.enqueue(new MockResponse()
//        .setHeader("Content-Type", "application/json")
//        .setResponseCode(HttpStatus.BAD_REQUEST.value())
//        .setBody("Invalid response body"));
//
//    // Perform the request
//    assertThrows(Exception.class, () -> oneMoneyAccountAggregator.initiateConsent(request));
//  }
//
//  @Test
//  public void testInitiateConsent_InvalidResponseTwo() {
//    // Prepare the request
//    OneMoneyConsentAPIRequest request = new OneMoneyConsentAPIRequest();
//    // Set up request with required data
//
//    // Enqueue an invalid response
//    mockWebServer.enqueue(new MockResponse()
//        .setHeader("Content-Type", "application/json")
//        .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//        .setBody("Invalid response body"));
//
//    // Perform the request
//    assertThrows(Exception.class, () -> oneMoneyAccountAggregator.initiateConsent(request));
//  }

  public static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
