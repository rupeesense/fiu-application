package com.rupeesense.fi.controllers;

import com.rupeesense.fi.aa.AccountAggregatorService;
import com.rupeesense.fi.request.ConsentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
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

    //TODO: change return type
    @PostMapping(path = "/consent",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String initiateConsentRequest(ConsentRequest request) {
        return accountAggregatorService.initiateConsent(request);
    }

}
