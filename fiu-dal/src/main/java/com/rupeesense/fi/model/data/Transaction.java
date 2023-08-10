package com.rupeesense.fi.model.data;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Model class for Transaction
 */
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Entity
@Table(
    name = "transaction",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"fip_id", "fip_txn_id"}
    )
)
public class Transaction {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @ManyToOne
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @EqualsAndHashCode.Include
  @Column(name = "fip_txn_id", updatable = false, nullable = false)
  private String fipTransactionId;

  @Column(updatable = false, nullable = false)
  private float amount;

  @Column(updatable = false, nullable = false)
  private String narration;

  @Enumerated(EnumType.STRING)
  @Column(updatable = false, nullable = false)
  private TransactionType transactionType;

  @Enumerated(EnumType.STRING)
  @Column(updatable = false, nullable = false)
  private PaymentMethod paymentMethod;

  @Column(updatable = false, nullable = false)
  private float currentBalance;

  @Column(updatable = false, nullable = false)
  private LocalDateTime transactionTimeStamp;

  @Column(updatable = false, nullable = false)
  private LocalDateTime valueDate;

  @Column(updatable = false, nullable = false)
  private String referenceNumber;

  @EqualsAndHashCode.Include
  @Column(name = "fip_id", nullable = false)
  private String fipID;

  @Column(updatable = false, nullable = false)
  private String userId;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

}
