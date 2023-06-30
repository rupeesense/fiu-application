package com.rupeesense.fi.aa;

import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.ext.onemoney.response.OneMoneyConsentAPIResponse;
import com.rupeesense.fi.model.Consent;
import com.rupeesense.fi.model.Consent.ConsentStatus;
import com.rupeesense.fi.repo.ConsentRepository;
import com.rupeesense.fi.request.ConsentRequest;
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

  public String initiateConsent(ConsentRequest consentRequest) {
    OneMoneyConsentAPIRequest oneMoneyConsentAPIRequest = OneMoneyConsentAPIRequest
        .generateConsentRequest("SEL0308", consentRequest.getUserVpa()); //TODO: don't hardcode
    OneMoneyConsentAPIResponse consentAPIResponse = oneMoneyAccountAggregator.initiateConsent(
        oneMoneyConsentAPIRequest);
    Consent consent = new Consent();
    consent.setConsentRequestId(consentAPIResponse.getHandle());
    consent.setUserId(consentAPIResponse.getCustomer().getId());
    consent.setStatus(ConsentStatus.PENDING);
    consentRepository.save(consent);
    return consent.getConsentRequestId();
  }
}
