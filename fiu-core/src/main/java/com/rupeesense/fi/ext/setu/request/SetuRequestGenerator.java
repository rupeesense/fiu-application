package com.rupeesense.fi.ext.setu.request;

import static com.rupeesense.fi.ext.onemoney.OneMoneyRequestGenerator.*;

import com.rupeesense.fi.FIUServiceConfig;
import com.rupeesense.fi.ext.ConsentDetail;
import com.rupeesense.fi.ext.ConsentDetail.DataConsumer;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyRequest.Customer;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyRequest.FIDataRange;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetuRequestGenerator {

  private final FIUServiceConfig fiuServiceConfig;

  @Autowired
  public SetuRequestGenerator(FIUServiceConfig fiuServiceConfig) {
    this.fiuServiceConfig = fiuServiceConfig;
  }


  public SetuConsentAPIRequest generateConsentRequest(String customerId) {
    LocalDateTime currentTime = LocalDateTime.now();

    ConsentDetail consentDetail = ConsentDetail.builder()
        .consentStart(currentTime)
        .consentExpiry(currentTime.plus(CONSENT_EXPIRY_DURATION))
        .consentMode(STORE_CONSENT_MODE)
        .fetchType(PERIODIC_FETCH_TYPE)
        .dataConsumer(new DataConsumer(fiuServiceConfig.getFiuId()))
        .customer(new Customer(customerId))
        .consentTypes(ALL_CONSENT_TYPES)
        .dataLife(DATA_LIFE_3_YEARS)
        .frequency(FREQUENCY_TWICE_IN_AN_HOUR)
        .purpose(PURPOSE_102)
        .fiDataRange(FIDataRange.builder()
            .from(currentTime.minus(CONSENT_EXPIRY_DURATION))
            .to(currentTime.plus(CONSENT_EXPIRY_DURATION))
            .build())
        .dataFilter(List.of(DATA_FILTER_TRANSACTION_AMOUNT_GREATER_THAN_1))
        .fiTypes(SUPPORTED_FI_TYPES)
        .build();

    return new SetuConsentAPIRequest("", new ArrayList<>(), consentDetail);
  }

}
