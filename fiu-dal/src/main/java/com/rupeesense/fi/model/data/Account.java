package com.rupeesense.fi.model.data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Entity
@Table(
    name = "account",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"userId", "fipID", "maskedAccountNumber"}
    )
)
public class Account {

  @Id
  private String accountId;

  private String fipID;
  private String maskedAccountNumber;
  private String linkRefNumber;
  private String branch;

  @Column(name = "currentODLimit", columnDefinition = "float")
  private float currentODLimit;

  @Column(name = "drawingLimit", columnDefinition = "float")
  private float drawingLimit;

  private String ifscCode;
  private String micrCode;
  private String currency;

  @Column(name = "balance", columnDefinition = "float")
  private float balance;

  private LocalDateTime openingDate;
  private LocalDateTime balanceDateTime;
  private LocalDateTime txnRefreshedAt;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  private Holding holding;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<AccountHolder> holders;

  @Enumerated(EnumType.STRING)
  private AccountType type;

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
    SAVINGS
  }
}
