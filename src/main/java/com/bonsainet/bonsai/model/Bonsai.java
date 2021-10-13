package com.bonsainet.bonsai.model;

import java.time.LocalDate;
import javax.persistence.*;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bonsai")
@Data
public class Bonsai {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private Integer tag;
  private Integer numberOfPlants;
  private String name;
  private String source;
  private String stateWhenAcquired;
  private LocalDate dateAcquired;
  private Float costAmount;
  private Integer yearStarted;
  private Boolean isYearStartedGuess;
  private Integer yearDied;
  private LocalDate dateSold;
  private Float soldForAmount;
  private String stage;
  private String style;
  private Boolean isGrafted;
  private Boolean isNoHoper;
  private String notes;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "taxonId")
  private Taxon taxon;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "userId")
  private User user;

  public Bonsai() {
  }

  public Bonsai(Integer id) {
    this.id = id;
  }

  public Taxon getTaxon() {
    return taxon;
  }

  public User getUser() {
    return user;
  }

}
