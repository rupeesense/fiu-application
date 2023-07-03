package com.rupeesense.fi.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "consent")
public class Consent {

  @Id
  @Column(name = "consent_id")
  private String consentId;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ConsentStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_aggregator", nullable = false)
  private AAIdentifier accountAggregator;

  @Lob
  @Column(name = "consent_request")
  private String consentArtifact;

  @Lob
  @Column(name = "digital_signature")
  private String digitalSignature;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

}
