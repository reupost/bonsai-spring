package com.bonsainet.bonsai.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "pic")
@Data
public class Pic {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String entityType; //TODO refactor into enum
  private Integer entityId;
  private String title;
  private Date dateTaken;
  private String filePath;

  public Pic() {
  }
}
