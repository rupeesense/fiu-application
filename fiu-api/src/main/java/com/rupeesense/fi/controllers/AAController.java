package com.rupeesense.fi.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that exposes endpoints to interact with Account Aggregators.
 */
@RestController
public class AAController {

    @PostMapping
    public ConsentResponse initiateConsentRequest(ConsentRequest request) {

    }

}
