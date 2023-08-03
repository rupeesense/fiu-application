package com.rupeesense.fi.ext.onemoney;

import com.rupeesense.fi.FIUServiceConfig;
import com.rupeesense.fi.ext.ConsentDetail;
import com.rupeesense.fi.ext.ConsentDetail.Category;
import com.rupeesense.fi.ext.ConsentDetail.ConsentType;
import com.rupeesense.fi.ext.ConsentDetail.DataFilter;
import com.rupeesense.fi.ext.ConsentDetail.DataLife;
import com.rupeesense.fi.ext.ConsentDetail.FIType;
import com.rupeesense.fi.ext.ConsentDetail.Frequency;
import com.rupeesense.fi.ext.ConsentDetail.Purpose;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OneMoneyRequestGenerator {


  //reference: https://api.rebit.co.in/purpose
  public static final Purpose PURPOSE_102 = ConsentDetail.Purpose.builder().code("102")
      .refUri("https://api.rebit.org.in/aa/purpose/102.xml")
      .text("Customer spending patterns, budget or other reportings")
      .category(new Category("Purpose Category")) //TODO: REVIEW
      .build();

  public static final Frequency FREQUENCY_TWICE_IN_AN_HOUR = ConsentDetail.Frequency.builder()
      .unit("HOUR").value(1).build();

  public static final DataLife DATA_LIFE_3_YEARS = new DataLife("YEAR", 3);

  public static final DataFilter DATA_FILTER_TRANSACTION_AMOUNT_GREATER_THAN_1 = new DataFilter(
      "TRANSACTIONAMOUNT", ">", "1");

  public static final String STORE_CONSENT_MODE = "STORE";

  public static final String PERIODIC_FETCH_TYPE = "PERIODIC";

  public static final List<ConsentType> ALL_CONSENT_TYPES = List.of(ConsentDetail.ConsentType.values());

  //TODO: might need to comeback in future and change this
  public static final List<FIType> SUPPORTED_FI_TYPES = List.of(FIType.DEPOSIT);

  public static final TemporalAmount CONSENT_EXPIRY_DURATION = Duration.ofDays(365);

  private final FIUServiceConfig fiuServiceConfig;

  @Autowired
  public OneMoneyRequestGenerator(FIUServiceConfig fiuServiceConfig) {
    this.fiuServiceConfig = fiuServiceConfig;
  }

}
