package com.rupeesense.fi.ext.onemoney;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

  public static String writeValueAsStringSilently(ObjectMapper objectMapper, Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException ex) {
      log.error("Error while serializing object: {}", object.getClass(), ex);
      return null;
    }
  }

}
