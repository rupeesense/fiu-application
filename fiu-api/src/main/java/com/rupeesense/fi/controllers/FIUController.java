package com.rupeesense.fi.controllers;

import com.rupeesense.fi.api.request.ConsentNotificationRequest;
import com.rupeesense.fi.fiu.FIUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to act as an FIU
 */
@RestController
@RequestMapping("/v1/fiu")
public class FIUController {

  private final FIUService fiuService;

  @Autowired
  public FIUController(FIUService fiuService) {
    this.fiuService = fiuService;
  }

  @PostMapping(path = "/notification/consent",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void receiveConsentNotification(ConsentNotificationRequest consentNotificationRequest) {
    fiuService.receiveConsentNotification(consentNotificationRequest);
  }
}
