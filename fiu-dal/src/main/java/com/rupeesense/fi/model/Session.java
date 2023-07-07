package com.rupeesense.fi.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "session")
public class Session {

  @Id
  private String id;

  //TODO: add lazy loading using hibernate
  @ManyToOne
  @JoinColumn(name = "consent_id")
  private Consent consent;

  @Column(name = "userId")
  private String userId;

  @Column(name = "requested_at")
  private LocalDateTime requestedAt;

  @Column(name = "status")
  private SessionStatus status;

  //created and updated fields
  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
