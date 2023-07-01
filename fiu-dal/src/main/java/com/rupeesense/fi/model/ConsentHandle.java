package com.rupeesense.fi.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consent_handle")
public class ConsentHandle {

  @Id
  @Column(name = "consent_handle_id", nullable = false)
  private String consentHandleId;

  @OneToOne
  @JoinColumn(name = "consent_id")
  private Consent consent;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ConsentHandleStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_aggregator", nullable = false)
  private AccountAggregatorIdentifier accountAggregator;

  @Lob
  @Column(name = "consent_request")
  private String consentRequest;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

}
