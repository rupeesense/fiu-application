package com.rupeesense.fi.ext.onemoney;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rupeesense.fi.FIUServiceConfig;
import com.rupeesense.fi.ext.onemoney.request.FIDataRequest;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyConsentAPIRequest;
import com.rupeesense.fi.ext.onemoney.request.ConsentDetail;
import com.rupeesense.fi.model.Consent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OneMoneyRequestGeneratorTest {

  @Mock
  private FIUServiceConfig fiuServiceConfig;

  private OneMoneyRequestGenerator requestGenerator;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    requestGenerator = new OneMoneyRequestGenerator(fiuServiceConfig);
  }

  @Test
  public void testGenerateDataRequest_Success() {
    // Prepare the input
    Consent consent = new Consent();
    consent.setConsentId("consent123");
    consent.setDigitalSignature("signature123");

    // Prepare the current time
    LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("UTC"));
    LocalDateTime expectedStartTime = currentTime.minusHours(1);
    LocalDateTime expectedEndTime = currentTime.minusMinutes(15);

    // Perform the data request generation
    FIDataRequest dataRequest = requestGenerator.generateDataRequest(consent);

    // Verify the generated data request
    assertNotNull(dataRequest);
    assertEquals(consent.getConsentId(), dataRequest.getConsent().getId());
    assertEquals(consent.getDigitalSignature(), dataRequest.getConsent().getDigitalSignature());
    assertNotNull(dataRequest.getKeyMaterial());
    assertNotNull(dataRequest.getFidataRange());
  }

  @Test
  public void testGeneratePeriodicConsentRequestForUser_Success() {
    // Prepare the input
    String customerId = "customer123";

    // Prepare the current time
    LocalDateTime currentTime = LocalDateTime.now();

    // Perform the periodic consent request generation
    OneMoneyConsentAPIRequest consentRequest = requestGenerator.generatePeriodicConsentRequestForUser(customerId);

    // Verify the generated consent request
    assertNotNull(consentRequest);
    ConsentDetail consentDetail = consentRequest.getConsentDetail();
    assertNotNull(consentDetail);
    assertEquals(OneMoneyRequestGenerator.STORE_CONSENT_MODE, consentDetail.getConsentMode());
    assertEquals(OneMoneyRequestGenerator.PERIODIC_FETCH_TYPE, consentDetail.getFetchType());
    assertNotNull(consentDetail.getDataConsumer());
    assertEquals(fiuServiceConfig.getFiuId(), consentDetail.getDataConsumer().getId());
    assertNotNull(consentDetail.getCustomer());
    assertEquals(customerId, consentDetail.getCustomer().getId());
    assertEquals(OneMoneyRequestGenerator.ALL_CONSENT_TYPES, consentDetail.getConsentTypes());
    assertEquals(OneMoneyRequestGenerator.DATA_LIFE_3_YEARS, consentDetail.getDataLife());
    assertEquals(OneMoneyRequestGenerator.FREQUENCY_TWICE_IN_AN_HOUR, consentDetail.getFrequency());
    assertEquals(OneMoneyRequestGenerator.PURPOSE_102, consentDetail.getPurpose());
    assertNotNull(consentDetail.getFiDataRange());
    assertEquals(List.of(OneMoneyRequestGenerator.DATA_FILTER_TRANSACTION_AMOUNT_GREATER_THAN_1),
        consentDetail.getDataFilter());
    assertEquals(OneMoneyRequestGenerator.SUPPORTED_FI_TYPES, consentDetail.getFiTypes());
  }
}
