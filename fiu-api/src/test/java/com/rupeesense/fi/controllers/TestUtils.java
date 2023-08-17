package com.rupeesense.fi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

  // Helper method to convert object to JSON string
  static String asJsonString(Object obj) {
    try {
      ObjectMapper ob = new ObjectMapper().findAndRegisterModules();
      return ob.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
