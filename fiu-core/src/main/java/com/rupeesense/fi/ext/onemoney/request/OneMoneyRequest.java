package com.rupeesense.fi.ext.onemoney.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupeesense.fi.ext.onemoney.OneMoneyUtils;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OneMoneyRequest {

  @JsonProperty("ver")
  private String ver;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  @JsonProperty("timestamp")
  private LocalDateTime timestamp;

  @JsonProperty("txnid")
  private String txnid;

  public OneMoneyRequest() {
    this.ver = OneMoneyUtils.getOneMoneyApiVersion();
    this.timestamp = OneMoneyUtils.getOneMoneyApiTimestamp();
    this.txnid = OneMoneyUtils.generateUUID();
  }

  @Builder
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FIDataRange {

    @JsonProperty("from")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime from;

    @JsonProperty("to")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime to;
  }

  @NoArgsConstructor
  @Setter
  @Getter
  @AllArgsConstructor
  public static class Customer {

    @JsonProperty("id")
    private String id;
  }

}
