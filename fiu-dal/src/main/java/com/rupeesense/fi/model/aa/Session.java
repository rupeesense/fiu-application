package com.rupeesense.fi.model.aa;

import com.rupeesense.fi.model.aa.AAIdentifier;
import com.rupeesense.fi.model.aa.Consent;
import com.rupeesense.fi.model.aa.SessionStatus;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "session",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"account_aggregator", "sessionId"}
    ))
public class Session {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "sessionId", updatable = false, nullable = false)
  private String sessionId;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_aggregator", updatable = false, nullable = false)
  private AAIdentifier accountAggregator;

  @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.MERGE)
  @JoinColumn(name = "consent_id")
  private Consent consent;

  @Column(name = "userId", nullable = false)
  private String userId;

  @Column(name = "requested_at")
  private LocalDateTime requestedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private SessionStatus status;

  //created and updated fields
  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
