package com.rupeesense.fi.model.data;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account_holder")
public class AccountHolder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String address;
  private String ckycCompliance;
  private String dob;
  private String email;
  private String mobile;
  private String name;
  private String nominee;
  private String pan;

  @ManyToOne
  @JoinColumn(name = "accountId", nullable = false)
  private Account account;
}
