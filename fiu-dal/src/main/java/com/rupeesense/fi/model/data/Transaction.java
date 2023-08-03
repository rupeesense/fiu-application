package com.rupeesense.fi.model.data;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for Transaction
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "transaction",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"accountId", "externalTransactionId"}
    )
)
public class Transaction {

  @Id
  private String id;

  @ManyToOne
  @JoinColumn(name = "accountId", nullable = false)
  private Account account;

  private String externalTransactionId;

  private float amount;

  private String narration;

  private TransactionType transactionType;

  private PaymentMethod paymentMethod;

  private float currentBalance;

  private LocalDateTime transactionTimeStamp;

  private LocalDateTime valueDate;

  private String referenceNumber;

  private String userId;

}
