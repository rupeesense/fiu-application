package com.rupeesense.fi.aa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.ext.onemoney.OneMoneyRequestGenerator;
import com.rupeesense.fi.ext.onemoney.request.FIDataRequest;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyRequest.Customer;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentArtifactAPIResponse;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentInitiateAPIResponse;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyRequestDataAPIResponse;
import com.rupeesense.fi.model.AAIdentifier;
import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.model.ConsentHandleStatus;
import com.rupeesense.fi.model.Session;
import com.rupeesense.fi.model.SessionStatus;
import com.rupeesense.fi.repo.RepositoryFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccountAggregatorOrchestratorServiceTest {

  @Mock
  private OneMoneyAccountAggregator oneMoneyAccountAggregator;
  @Mock
  private RepositoryFacade repositoryFacade;
  @Mock
  private OneMoneyRequestGenerator oneMoneyRequestGenerator;
  @Mock
  private ObjectMapper objectMapper;

  private AccountAggregatorOrchestratorService orchestratorService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    orchestratorService = new AccountAggregatorOrchestratorService(
        oneMoneyAccountAggregator, repositoryFacade, oneMoneyRequestGenerator, objectMapper);
  }

//  @Test
//  public void testInitiateConsent_Success() {
//    // Prepare the input
//    ConsentRequest consentRequest = new ConsentRequest("test@aa", AAIdentifier.ONEMONEY);
//    // Set up consentRequest with required data
//
//    // Prepare the mocked objects
//    OneMoneyConsentAPIRequest consentAPIRequest = new OneMoneyConsentAPIRequest();
//    OneMoneyConsentInitiateAPIResponse consentAPIResponse = new OneMoneyConsentInitiateAPIResponse();
//    consentAPIResponse.setHandle("handle123");
//    consentAPIResponse.setCustomer(new Customer("cust123"));
//    // Set up the mocked interactions
//    when(oneMoneyRequestGenerator.generatePeriodicConsentRequestForUser(anyString()))
//        .thenReturn(consentAPIRequest);
//    when(oneMoneyAccountAggregator.initiateConsent(eq(consentAPIRequest)))
//        .thenReturn(consentAPIResponse);
//
//    // Perform the initiation
//    ConsentResponse response = orchestratorService.initiateConsent(consentRequest);
//
//    // Verify the interactions and assertions
//    verify(oneMoneyRequestGenerator).generatePeriodicConsentRequestForUser(eq(consentRequest.getUserVpa()));
//    verify(oneMoneyAccountAggregator).initiateConsent(eq(consentAPIRequest));
//    verifyNoMoreInteractions(oneMoneyRequestGenerator, oneMoneyAccountAggregator);
//    assertEquals(consentAPIResponse.getCustomer().getId(), response.getUserVpa());
//    assertEquals(consentRequest.getAccountAggId(), response.getAccountAggId());
//    assertEquals(ConsentHandleStatus.PENDING, response.getStatus());
//  }

  @Test
  public void testFetchConsentArtifact_Success() {
    String consentId = "consent123";

    OneMoneyConsentArtifactAPIResponse expectedResponse = new OneMoneyConsentArtifactAPIResponse();
    // Set up expected response

    when(oneMoneyAccountAggregator.getConsentArtifact(eq(consentId)))
        .thenReturn(expectedResponse);

    OneMoneyConsentArtifactAPIResponse response = orchestratorService.fetchConsentArtifact(consentId);

    verify(oneMoneyAccountAggregator).getConsentArtifact(eq(consentId));
    verifyNoMoreInteractions(oneMoneyAccountAggregator);
    assertEquals(expectedResponse, response);
  }

  @Test
  public void testPlaceDataRequest_Success() {
    String userId = "user123";

    Consent consent = new Consent();
    consent.setUserId(userId);

    FIDataRequest dataRequest = new FIDataRequest();
    OneMoneyRequestDataAPIResponse expectedResponse = new OneMoneyRequestDataAPIResponse();

    when(repositoryFacade.findActiveConsentByUserId(eq(userId)))
        .thenReturn(consent);
    when(oneMoneyRequestGenerator.generateDataRequest(eq(consent)))
        .thenReturn(dataRequest);
    when(oneMoneyAccountAggregator.placeDataRequest(eq(dataRequest)))
        .thenReturn(expectedResponse);

    Session session = orchestratorService.placeDataRequest(userId);

    verify(repositoryFacade).findActiveConsentByUserId(eq(userId));
    verify(oneMoneyRequestGenerator).generateDataRequest(eq(consent));
    verify(oneMoneyAccountAggregator).placeDataRequest(eq(dataRequest));
    verify(repositoryFacade).save(eq(session));
    verifyNoMoreInteractions(repositoryFacade, oneMoneyRequestGenerator, oneMoneyAccountAggregator);
    assertEquals(expectedResponse.getSessionId(), session.getId());
    assertEquals(SessionStatus.AWAITED, session.getStatus());
    assertEquals(userId, session.getUserId());
    assertEquals(consent, session.getConsent());
  }

  @Test
  public void testPlaceDataRequest_NoActiveConsent() {
    String userId = "user123";

    when(repositoryFacade.findActiveConsentByUserId(eq(userId)))
        .thenReturn(null);

    assertThrows(IllegalStateException.class, () -> orchestratorService.placeDataRequest(userId));

    verify(repositoryFacade).findActiveConsentByUserId(eq(userId));
    verifyNoMoreInteractions(repositoryFacade, oneMoneyRequestGenerator, oneMoneyAccountAggregator);
  }
}