package com.rupeesense.fi.ext.setu.request;

import static com.rupeesense.fi.ext.commons.ConsentRequestDefaults.ALL_CONSENT_TYPES;
import static com.rupeesense.fi.ext.commons.ConsentRequestDefaults.CONSENT_EXPIRY_DURATION;
import static com.rupeesense.fi.ext.commons.ConsentRequestDefaults.DATA_FILTER_TRANSACTION_AMOUNT_GREATER_THAN_1;
import static com.rupeesense.fi.ext.commons.ConsentRequestDefaults.DATA_LIFE_3_YEARS;
import static com.rupeesense.fi.ext.commons.ConsentRequestDefaults.FREQUENCY_TWICE_IN_AN_HOUR;
import static com.rupeesense.fi.ext.commons.ConsentRequestDefaults.PERIODIC_FETCH_TYPE;
import static com.rupeesense.fi.ext.commons.ConsentRequestDefaults.PURPOSE_102;
import static com.rupeesense.fi.ext.commons.ConsentRequestDefaults.STORE_CONSENT_MODE;
import static com.rupeesense.fi.ext.commons.ConsentRequestDefaults.SUPPORTED_FI_TYPES;

import com.rupeesense.fi.FIUServiceConfig;
import com.rupeesense.fi.ext.ConsentDetail;
import com.rupeesense.fi.ext.ConsentDetail.DataConsumer;
import com.rupeesense.fi.ext.commons.FIDataRange;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyRequest.Customer;
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


  public SetuDataRequest generateDataRequest(String consentId, LocalDateTime from, LocalDateTime to) {
    return SetuDataRequest.builder()
        .consentId(consentId)
        .dataRange(FIDataRange.builder()
            .from(from)
            .to(to)
            .build())
        .format("json")
        .build();
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
