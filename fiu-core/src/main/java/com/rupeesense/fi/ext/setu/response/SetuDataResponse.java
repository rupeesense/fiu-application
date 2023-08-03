package com.rupeesense.fi.ext.setu.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.model.SessionStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetuDataResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("status")
  private SessionStatus status;

  @JsonProperty("Payload")
  private List<DataPayload> dataPayload;


  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class DataPayload {

    @JsonProperty("fipID")
    private String fipId;

    @JsonProperty("data")
    private List<AccountLevelData> data;
  }

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class AccountLevelData {

    @JsonProperty("linkRefNumber")
    private String linkRefNumber;

    @JsonProperty("maskedAccNumber")
    private String maskedAccNumber;

    @JsonProperty("decryptedFI")
    private FinancialInformationPayload informationPayload;


  }

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class FinancialInformationPayload {

    private AccountData account;

  }

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class AccountData {

    @JsonProperty("linkedAccRef")
    private String linkedAccRef;

    @JsonProperty("maskedAccNumber")
    private String maskedAccNumber;

    @JsonProperty("type")
    private String type;

    @JsonProperty("profile")
    private Profile profile;

    @JsonProperty("summary")
    private Summary summary;

    @JsonProperty("transactions")
    private Transactions transactions;

  }


  @Setter
  @NoArgsConstructor
  @Getter
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Profile {

    @JsonProperty("holders")
    private Holders holders;

    @Setter
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Holders {

      @JsonProperty("type")
      private String type;

      @JsonProperty("holder")
      private List<Holder> holder;
    }

    @Setter
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Holder {

      @JsonProperty("address")
      private String address;

      @JsonProperty("ckycCompliance")
      private String ckycCompliance;

      @JsonProperty("dob")
      private String dob;

      @JsonProperty("email")
      private String email;

      @JsonProperty("mobile")
      private String mobile;

      @JsonProperty("name")
      private String name;

      @JsonProperty("nominee")
      private String nominee;

      @JsonProperty("pan")
      private String pan;
    }
  }

  @Setter
  @NoArgsConstructor
  @Getter
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Summary {

    @JsonProperty("balanceDateTime")
    private String balanceDateTime;

    @JsonProperty("branch")
    private String branch;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("currentBalance")
    private String currentBalance;

    @JsonProperty("currentODLimit")
    private String currentODLimit;

    @JsonProperty("drawingLimit")
    private String drawingLimit;

    @JsonProperty("exchgeRate")
    private String exchgeRate;

    @JsonProperty("facility")
    private String facility;

    @JsonProperty("ifscCode")
    private String ifscCode;

    @JsonProperty("micrCode")
    private String micrCode;

    @JsonProperty("openingDate")
    private String openingDate;

    @JsonProperty("pending")
    private Pending pending;

    @JsonProperty("status")
    private String status;

    @JsonProperty("type")
    private String type;

    @Setter
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pending {

      @JsonProperty("amount")
      private String amount;

      @JsonProperty("transactionType")
      private String transactionType;
    }
  }


  @Setter
  @NoArgsConstructor
  @Getter
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Transactions {

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("transaction")
    private List<Transaction> transaction;

    @Setter
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transaction {

      @JsonProperty("amount")
      private String amount;

      @JsonProperty("currentBalance")
      private String currentBalance;

      @JsonProperty("mode")
      private String mode;

      @JsonProperty("narration")
      private String narration;

      @JsonProperty("reference")
      private String reference;

      @JsonProperty("transactionTimestamp")
      private String transactionTimestamp;

      @JsonProperty("txnId")
      private String txnId;

      @JsonProperty("type")
      private String type;

      @JsonProperty("valueDate")
      private String valueDate;
    }
  }

}
