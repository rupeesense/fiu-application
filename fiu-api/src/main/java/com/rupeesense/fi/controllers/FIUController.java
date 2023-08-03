package com.rupeesense.fi.controllers;

import static com.rupeesense.fi.APIConstants.AA_CONSENT_NOTIFICATION;
import static com.rupeesense.fi.APIConstants.FIU_CONSENT_CREATE;
import static com.rupeesense.fi.APIConstants.FIU_DATA_REQUEST_CREATE;

import com.rupeesense.fi.api.request.NotificationEvent;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.request.DataRequest;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.fiu.FIUService;
import com.rupeesense.fi.model.Session;
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

  @PostMapping(path = FIU_CONSENT_CREATE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsentResponse createConsent(@RequestBody @Valid ConsentRequest consentRequest) {
      return fiuService.createConsent(consentRequest);
    }

  @PostMapping(path = AA_CONSENT_NOTIFICATION,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void receiveNotification(@RequestBody @Valid NotificationEvent notificationEvent) {
    fiuService.updateConsent(notificationEvent);
  }


  @PostMapping(path = FIU_DATA_REQUEST_CREATE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Session raiseDataRequest(@RequestBody @Valid DataRequest dataRequest) {
    return fiuService.createDataRequest(dataRequest);
  }
}
