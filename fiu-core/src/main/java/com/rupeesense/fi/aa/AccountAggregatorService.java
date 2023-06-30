package com.rupeesense.fi.aa;

import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.request.ConsentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountAggregatorService {

  OneMoneyAccountAggregator oneMoneyAccountAggregator;

  @Autowired
  public AccountAggregatorService(OneMoneyAccountAggregator oneMoneyAccountAggregator) {
    this.oneMoneyAccountAggregator = oneMoneyAccountAggregator;
  }

  public String initiateConsent(ConsentRequest consentRequest) {
    OneMoneyConsentAPIRequest oneMoneyConsentAPIRequest = OneMoneyConsentAPIRequest.generateConsentRequest("SEL0308", consentRequest.getUserVpa()); //TODO: don't hardcode
    return oneMoneyAccountAggregator.initiateConsent(oneMoneyConsentAPIRequest).getHandle();
  }
}
