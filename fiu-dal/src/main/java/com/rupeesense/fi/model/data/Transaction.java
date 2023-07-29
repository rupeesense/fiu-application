package com.rupeesense.fi.model.data;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Model class for Transaction
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

  private String transactionId;

  private float amount;

  private float narration;

  private TransactionType transactionType;

  private PaymentMethod paymentMethod;

  private float currentBalance;

  private LocalDateTime transactionTimeStamp;

  private LocalDateTime valueDate;

  private String referenceNumber;

}
