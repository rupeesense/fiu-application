package com.rupeesense.fi.aa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.ext.onemoney.OneMoneyRequestGenerator;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.ext.onemoney.request.FIDataRequest;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentAPIResponse;
import com.rupeesense.fi.model.AccountAggregatorIdentifier;
import com.rupeesense.fi.model.ConsentHandle;
import com.rupeesense.fi.model.ConsentStatus;
import com.rupeesense.fi.repo.ConsentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountAggregatorService {

  private final OneMoneyAccountAggregator oneMoneyAccountAggregator;

  private final ConsentRepository consentRepository;

  private final OneMoneyRequestGenerator oneMoneyRequestGenerator;

  private final ObjectMapper objectMapper;

  @Autowired
  public AccountAggregatorService(OneMoneyAccountAggregator oneMoneyAccountAggregator,
      ConsentRepository consentRepository, OneMoneyRequestGenerator oneMoneyRequestGenerator,
      ObjectMapper objectMapper) {
    this.oneMoneyAccountAggregator = oneMoneyAccountAggregator;
    this.consentRepository = consentRepository;
    this.oneMoneyRequestGenerator = oneMoneyRequestGenerator;
    this.objectMapper = objectMapper;
  }

  public ConsentResponse initiateConsent(ConsentRequest consentRequest) {
    OneMoneyConsentAPIRequest oneMoneyConsentAPIRequest = oneMoneyRequestGenerator
        .generatePeriodicConsentRequestForUser(consentRequest.getUserVpa());
    OneMoneyConsentAPIResponse consentAPIResponse = oneMoneyAccountAggregator
        .initiateConsent(oneMoneyConsentAPIRequest);
    ConsentHandle consentHandle = createAndSaveConsentHandle(oneMoneyConsentAPIRequest, consentAPIResponse);
    return new ConsentResponse(consentHandle.getUserId(), consentRequest.getAccountAggId(),
        consentHandle.getConsentHandleId(), consentHandle.getStatus());
  }

  public void placeDataRequest(FIDataRequest dataRequest) {

  }

  private ConsentHandle createAndSaveConsentHandle(OneMoneyConsentAPIRequest oneMoneyConsentAPIRequest,
      OneMoneyConsentAPIResponse consentAPIResponse) {
    ConsentHandle consentHandle = new ConsentHandle();
    consentHandle.setConsentHandleId(consentAPIResponse.getHandle());
    consentHandle.setUserId(consentAPIResponse.getCustomer().getId());
    consentHandle.setAccountAggregator(AccountAggregatorIdentifier.ONEMONEY);
    try {
      consentHandle.setConsentRequest(objectMapper.writeValueAsString(oneMoneyConsentAPIRequest));
    } catch (JsonProcessingException ex) {
      log.error("Error while serializing consent request: {}", oneMoneyConsentAPIRequest, ex);
    }
    consentHandle.setStatus(ConsentStatus.PENDING);
    consentRepository.save(consentHandle);
    return consentHandle;
  }
}
