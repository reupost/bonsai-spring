package com.bonsainet.bonsai.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String userName;
  private LocalDate dateRegistered;
  private String email;
  private String bio;

  public User() {
  }

  public User(Integer id) {
    this.id = id;
  }

}
