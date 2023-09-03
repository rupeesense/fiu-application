package com.rupeesense.fi.controllers;

import com.rupeesense.fi.MerchantResponse;
import com.rupeesense.fi.model.data.Transaction;
import com.rupeesense.fi.repo.TransactionRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/transaction")
public class TransactionController {

  private final TransactionRepository transactionRepository;

  @Autowired
  public TransactionController(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @GetMapping(path = "/merchant/spend", produces = MediaType.APPLICATION_JSON_VALUE)
  public MerchantResponse getMerchantSpending(@RequestParam("q") String merchantQuery,
      @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
    List<Transaction> merchantTransactions = transactionRepository
        .getTransactionsByNarrationLike(merchantQuery);

    Integer sum = merchantTransactions.stream()
        .filter(txn -> txn.getTransactionTimeStamp().isAfter(from.atStartOfDay()) &&
            txn.getTransactionTimeStamp().isBefore(to.atStartOfDay()))
        .mapToInt(txn -> (int) txn.getAmount()).sum();

    return new MerchantResponse(sum);
  }

}
