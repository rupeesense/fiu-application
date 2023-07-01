package com.rupeesense.fi.ext.onemoney;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OneMoneyUtils {

  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }

  public static LocalDateTime getOneMoneyApiTimestamp() {
    return LocalDateTime.now(ZoneId.of("UTC"));
  }

  public static String getOneMoneyApiVersion() {
    return "1.1.2";
  }

}
