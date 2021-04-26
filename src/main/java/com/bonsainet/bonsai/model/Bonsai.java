package com.bonsainet.bonsai.model;

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
  private Date dateAcquired;
  private Float costAmount;
  private Integer yearStarted;
  private Boolean isYearStartedGuess;
  private Integer yearDied;
  private Date dateSold;
  private Float soldForAmount;
  private String stage;
  private String style;
  private Boolean isGrafted;
  private Boolean isNoHoper;
  private String notes;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "taxonId")
  private Taxon taxon;

  public Bonsai() {
  }

  public Bonsai(Integer id, Integer taxonId, Integer tag) {
    this.id = id;
    this.tag = tag;
  }

  public Taxon getTaxon() {
    return taxon;
  }

}
