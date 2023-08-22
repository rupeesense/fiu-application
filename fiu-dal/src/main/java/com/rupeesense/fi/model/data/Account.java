package com.rupeesense.fi.model.data;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(
    name = "account",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"userId", "fip_id", "linkRefNumber"}
    )
)
public class Account {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "accountId", updatable = false, nullable = false)
  private String accountId;

  @Column(name = "fip_id", nullable = false)
  private String fipID;

  @Column(nullable = false)
  private String maskedAccountNumber;

  @Column(nullable = false)
  private String linkRefNumber;

  private String branch;

  @Column(name = "currentODLimit", columnDefinition = "float")
  private float currentODLimit;

  @Column(name = "drawingLimit", columnDefinition = "float")
  private float drawingLimit;

  private String ifscCode;
  private String micrCode;
  private String currency;

  @Column(name = "balance", columnDefinition = "float", nullable = false)
  private float balance;

  private LocalDateTime openingDate;

  @Column(nullable = false)
  private LocalDateTime balanceDateTime;

  @Column(nullable = false)
  private LocalDateTime txnRefreshedAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Holding holding;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<AccountHolder> holders;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AccountType type;

  @Column(nullable = false)
  private String userId;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public enum Status {
    ACTIVE,
    INACTIVE
  }

  public enum Holding {
    SINGLE
  }

  public enum AccountType {
    DEPOSIT,
    SAVINGS
  }
}
