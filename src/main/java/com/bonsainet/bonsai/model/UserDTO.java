package com.bonsainet.bonsai.model;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UserDTO {

  private Integer id;

  private String userName;
  private String email;
  private String bio;

  @DateTimeFormat
  private LocalDateTime dateRegistered;

  // TODO omit unneeded properties

  public UserDTO() {
  }

  public UserDTO(Integer userId) {
    this.id = userId;
  }
}
