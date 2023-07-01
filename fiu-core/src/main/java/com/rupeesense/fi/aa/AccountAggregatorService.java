package com.rupeesense.fi.aa;

import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.request.OneMoneyRequestGenerator;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.ext.onemoney.request.FIDataRequest;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentAPIResponse;
import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.model.ConsentStatus;
import com.rupeesense.fi.repo.ConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountAggregatorService {

  private final OneMoneyAccountAggregator oneMoneyAccountAggregator;

  private final ConsentRepository consentRepository;

  @Autowired
  public AccountAggregatorService(OneMoneyAccountAggregator oneMoneyAccountAggregator,
      ConsentRepository consentRepository) {
    this.oneMoneyAccountAggregator = oneMoneyAccountAggregator;
    this.consentRepository = consentRepository;
  }

  public ConsentResponse initiateConsent(ConsentRequest consentRequest) {
    OneMoneyConsentAPIRequest oneMoneyConsentAPIRequest = OneMoneyRequestGenerator.generatePeriodicConsentRequest("RUP0328", consentRequest.getUserVpa()); //TODO: don't hardcode
    OneMoneyConsentAPIResponse consentAPIResponse = oneMoneyAccountAggregator
        .initiateConsent(oneMoneyConsentAPIRequest);
    Consent consent = new Consent();
    consent.setConsentRequestId(consentAPIResponse.getHandle());
    consent.setUserId(consentAPIResponse.getCustomer().getId());
    consent.setStatus(ConsentStatus.PENDING);
    consentRepository.save(consent);
    return new ConsentResponse(consent.getUserId(), consentRequest.getAccountAggId(),
        consent.getConsentRequestId(), consent.getStatus());
  }

  public void placeDataRequest(FIDataRequest dataRequest) {

  }
}
