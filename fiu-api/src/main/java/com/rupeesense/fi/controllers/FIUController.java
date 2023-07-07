package com.rupeesense.fi.controllers;

import static com.rupeesense.fi.APIConstants.AA_CONSENT_NOTIFICATION;

import com.rupeesense.fi.api.request.ConsentNotificationRequest;
import com.rupeesense.fi.fiu.FIUService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to act as an FIU
 */
@RestController
@RequestMapping
public class FIUController {

  private final FIUService fiuService;

  @Autowired
  public FIUController(FIUService fiuService) {
    this.fiuService = fiuService;
  }

  @PostMapping(path = AA_CONSENT_NOTIFICATION,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void receiveConsentNotification(@RequestBody @Valid ConsentNotificationRequest consentNotificationRequest) {
    fiuService.updateConsentAndHandleFromNotification(consentNotificationRequest);
  }
}
