package com.rupeesense.fi.controllers;

import static com.rupeesense.fi.controllers.APIConstants.ACCOUNT_AGGREGATOR_BASE_PATH;
import static com.rupeesense.fi.controllers.APIConstants.PLACE_DATA_REQUEST_PATH;
import static com.rupeesense.fi.controllers.APIConstants.RAISE_PERIODIC_CONSENT_PATH;

import com.rupeesense.fi.aa.AccountAggregatorOrchestratorService;
import com.rupeesense.fi.api.request.ConsentRequest;
import com.rupeesense.fi.api.response.ConsentResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that exposes endpoints to interact with Account Aggregators.
 */
@RestController
@RequestMapping(ACCOUNT_AGGREGATOR_BASE_PATH)
public class AAOrchestrationController {

    private AccountAggregatorOrchestratorService accountAggregatorOrchestratorService;

    @Autowired
    public AAOrchestrationController(AccountAggregatorOrchestratorService accountAggregatorOrchestratorService) {
        this.accountAggregatorOrchestratorService = accountAggregatorOrchestratorService;
    }

    @PostMapping(path = RAISE_PERIODIC_CONSENT_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ConsentResponse initiatePeriodicConsentRequest(@RequestBody @Valid ConsentRequest request) {
        return accountAggregatorOrchestratorService.initiateConsent(request);
    }

    @PostMapping(path = PLACE_DATA_REQUEST_PATH,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public void placeDataRequestForUser(@PathVariable("userId") @NotBlank String userId) {
         accountAggregatorOrchestratorService.placeDataRequest(userId);
    }
}
