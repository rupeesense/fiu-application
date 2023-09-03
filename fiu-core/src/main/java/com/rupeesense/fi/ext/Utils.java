package com.rupeesense.fi.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class Utils {

  public static String generateUUID() {
    return UUID.randomUUID().toString();
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
