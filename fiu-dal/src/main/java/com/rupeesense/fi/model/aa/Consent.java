package com.rupeesense.fi.model.aa;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consent",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"account_aggregator", "consent_id"}
    ))
public class Consent {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "consent_id", updatable = false, nullable = false)
  private String consentId;

  @Column(name = "user_id", updatable = false, nullable = false)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ConsentStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_aggregator", updatable = false, nullable = false)
  private AAIdentifier accountAggregator;

  @Lob
  @Column(name = "consent_request")
  private String consentArtifact;

  @Enumerated(EnumType.STRING)
  @Column(name = "data_fetch_freq_unit")
  private DataFrequencyUnit dataFetchFreqUnit;

  @Column(name = "data_fetch_freq_value")
  private Integer dataFetchFreqValue;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;


  @Getter
  @AllArgsConstructor
  public enum DataFrequencyUnit {

    HOUR(ChronoUnit.HOURS),
    DAILY(ChronoUnit.DAYS),
    WEEKLY(ChronoUnit.WEEKS),
    MONTHLY(ChronoUnit.MONTHS),
    YEARLY(ChronoUnit.YEARS);

    private final ChronoUnit chronoUnit;
  }

}
