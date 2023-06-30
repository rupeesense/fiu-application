package com.rupeesense.fi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consent")
public class Consent {

  @Id
  @Column(name = "consent_request_id", nullable = false)
  private String consentRequestId;

  @Column(name = "consent_id", nullable = false)
  private String consentId;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ConsentStatus status;

  public enum ConsentStatus {
    GRANTED,
    DENIED,
    PENDING
  }
}
