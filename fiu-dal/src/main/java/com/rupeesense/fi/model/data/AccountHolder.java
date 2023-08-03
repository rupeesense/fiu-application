package com.rupeesense.fi.model.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
