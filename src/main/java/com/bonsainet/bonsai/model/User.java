package com.bonsainet.bonsai.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "user")
@Data
@Slf4j
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String userName;
  private String email;
  private String bio;

  @DateTimeFormat
  private LocalDateTime dateRegistered;

  public User() {
  }

  public User(Integer id) {
    this.id = id;
  }

}
