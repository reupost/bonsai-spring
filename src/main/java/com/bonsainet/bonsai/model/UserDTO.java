package com.bonsainet.bonsai.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDTO {

  private Integer id;

  private String userName;
  private LocalDate dateRegistered;
  private String email;
  private String bio;
  // TODO omit unneeded properties

  public UserDTO() {
  }

  public UserDTO(Integer userId) {
    this.id = userId;
  }
}
