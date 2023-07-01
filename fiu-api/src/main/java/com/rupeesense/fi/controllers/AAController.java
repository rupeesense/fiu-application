package com.rupeesense.fi.controllers;

import com.rupeesense.fi.aa.AccountAggregatorService;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.response.ConsentResponse;
import com.rupeesense.fi.ext.onemoney.request.FIDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that exposes endpoints to interact with Account Aggregators.
 */
@RestController
@RequestMapping("/v1/account_aggregator")
public class AAController {

    private AccountAggregatorService accountAggregatorService;

    @Autowired
    public AAController(AccountAggregatorService accountAggregatorService) {
        this.accountAggregatorService = accountAggregatorService;
    }

    @PostMapping(path = "/consent/periodic",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsentResponse initiatePeriodicConsentRequest(@RequestBody ConsentRequest request) {
        return accountAggregatorService.initiateConsent(request);
    }

    @PostMapping(path = "/data/request",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public void placeDataRequest(@RequestBody FIDataRequest dataRequest) {
         accountAggregatorService.placeDataRequest(dataRequest);
    }

}
