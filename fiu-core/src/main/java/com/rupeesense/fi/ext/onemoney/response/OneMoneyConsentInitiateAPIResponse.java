package com.rupeesense.fi.ext.onemoney.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class OneMoneyConsentInitiateAPIResponse {

  @JsonProperty("ver")
  private String ver;

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("txnid")
  private String txnid;

  @JsonProperty("Customer")
  private Customer customer;

  @JsonProperty("ConsentHandle")
  private String handle;

  @NoArgsConstructor
  @Setter
  @Getter
  @AllArgsConstructor
  public static class Customer {

    @JsonProperty("id")
    private String id;
  }

}
