package com.rupeesense.fi.api.onemoney.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OneMoneyConsentAPIRequest {

  @JsonProperty("ver")
  private String ver;

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("txnid")
  private String txnid;

  @JsonProperty("ConsentDetail")
  private ConsentDetail consentDetail;

  @Getter
  @Builder
  public static class ConsentDetail {

    @JsonProperty("consentStart")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime consentStart;

    @JsonProperty("consentExpiry")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime consentExpiry;

    @JsonProperty("consentMode")
    private String consentMode; //could be VIEW, STORE, QUERY or STREAM

    @JsonProperty("fetchType")
    private String fetchType;

    @JsonProperty("consentTypes")
    private List<String> consentTypes;

    @JsonProperty("fiTypes")
    private List<FIType> fiTypes;

    @JsonProperty("DataConsumer")
    private DataConsumer dataConsumer;

    @JsonProperty("Customer")
    private Customer customer;

    @JsonProperty("Purpose")
    private Purpose purpose;

    @JsonProperty("FIDataRange")
    private FIDataRange fiDataRange;

    @JsonProperty("DataLife")
    private DataLife dataLife;

    @JsonProperty("Frequency")
    private Frequency frequency;

    @JsonProperty("DataFilter")
    private List<DataFilter> dataFilter;

  }

  public enum FIType {
    DEPOSIT, TERM_DEPOSIT, RECURRING_DEPOSIT, SIP, CP, GOVT_SECURITIES, EQUITIES, BONDS, DEBENTURES, MUTUAL_FUNDS, ETF, IDR, CIS, AIF, INSURANCE_POLICIES, NPS, INVIT, REIT, OTHER
  }

  @Getter
  @AllArgsConstructor
  public static class DataConsumer {

    @JsonProperty("id")
    private String id;
  }

  @Getter
  @AllArgsConstructor
  public static class Customer {

    @JsonProperty("id")
    private String id;
  }

  @Getter
  @Builder
  public static class Purpose {

    @JsonProperty("code")
    private String code;

    @JsonProperty("refUri")
    private String refUri;

    @JsonProperty("text")
    private String text;

    @JsonProperty("Category")
    private Category category;
  }

  @Getter
  @AllArgsConstructor
  public static class Category {

    @JsonProperty("type")
    private String type;
  }

  @Getter
  @AllArgsConstructor
  public static class FIDataRange {

    @JsonProperty("from")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime from;

    @JsonProperty("to")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime to;
  }

  @Getter
  @AllArgsConstructor
  public static class DataLife {

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("value")
    private int value;
  }

  @Getter
  @AllArgsConstructor
  public static class Frequency {

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("value")
    private int value;
  }

  @Getter
  @AllArgsConstructor
  public static class DataFilter {

    @JsonProperty("type")
    private String type;

    @JsonProperty("operator")
    private String operator;

    @JsonProperty("value")
    private String value;
  }

  //https://api.rebit.co.in/purpose
  public static final Purpose PURPOSE_102 = Purpose.builder().code("102")
      .refUri("https://api.rebit.org.in/aa/purpose/102.xml")
      .text("Customer spending patterns, budget or other reportings")
      .category(new Category("Purpose Category")) //TODO: REVIEW
      .build();


  public static OneMoneyConsentAPIRequest generateConsentRequest(String dataConsumerId, String customerId) {

    ConsentDetail consentDetail = ConsentDetail.builder()
        .consentStart(LocalDateTime.now())
        .consentExpiry(LocalDateTime.now().plusYears(1))
        .consentMode("STORE")
        .fetchType("PERIODIC")
        .dataConsumer(new DataConsumer(dataConsumerId))
        .customer(new Customer(customerId))
        .consentTypes(Arrays.asList("TRANSACTIONS", "PROFILE", "SUMMARY"))
        .dataLife(new DataLife("YEAR", 3))
        .frequency(new Frequency("DATE", 2))
        .purpose(PURPOSE_102)
        .fiDataRange(new FIDataRange(LocalDateTime.now().minusDays(10), LocalDateTime.now()))
        .fiTypes(List.of(FIType.values())) //use all FI types //TODO: verify
        .build();

    return new OneMoneyConsentAPIRequest("1.1.2",
        "timestamp",
        "txnID", consentDetail);
  }

}
