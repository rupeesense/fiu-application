package com.rupeesense.fi.ext.onemoney.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyRequest.Customer;
import com.rupeesense.fi.ext.onemoney.request.OneMoneyRequest.FIDataRange;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentDetail {

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
  private List<ConsentType> consentTypes;

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

  public enum FIType {
    DEPOSIT, TERM_DEPOSIT, RECURRING_DEPOSIT, SIP, CP, GOVT_SECURITIES, EQUITIES, BONDS, DEBENTURES, MUTUAL_FUNDS, ETF, IDR, CIS, AIF, INSURANCE_POLICIES, NPS, INVIT, REIT, OTHER
  }

  public enum ConsentType {
    TRANSACTIONS,
    PROFILE,
    SUMMARY
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DataConsumer {

    @JsonProperty("id")
    private String id;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
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
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Category {

    @JsonProperty("type")
    private String type;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DataLife {

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("value")
    private int value;
  }

  @Builder
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Frequency {

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("value")
    private int value;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DataFilter {

    @JsonProperty("type")
    private String type;

    @JsonProperty("operator")
    private String operator;

    @JsonProperty("value")
    private String value;
  }
}
