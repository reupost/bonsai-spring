package com.bonsainet.taxon.model;


import javax.persistence.*;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bonsai")
public class Bonsai {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer id;

  //public Integer taxonId;
  public Integer tag;
  public Integer numberOfPlants;
  public String name;
  public String source;
  public String stateWhenAcquired;
  public Date dateAcquired;
  public Float costAmount;
  public Integer yearStarted;
  public Boolean isYearStartedGuess;
  public Integer yearDied;
  public Date dateSold;
  public Float soldForAmount;
  public String stage;
  public String style;
  public Boolean isGrafted;
  public Boolean isNoHoper;
  public String notes;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "taxonId")
  public Taxon taxon;

  public Bonsai() {
  }

  public Bonsai(Integer id, Integer taxonId, Integer tag) {
    this.id = id;
    //this.taxonId = taxonId;
    this.tag = tag;
  }

  public Taxon getTaxon() {
    return taxon;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Bonsai{");
    sb.append("id=").append(id);
    //sb.append(", taxonId=").append(taxonId);
    sb.append(", taxonId=").append(taxon.id);
    sb.append(", tag=").append(tag);
    sb.append(", numberOfPlants=").append(numberOfPlants);
    sb.append(", name='").append(name).append('\'');
    sb.append(", source='").append(source).append('\'');
    sb.append(", stateWhenAcquired='").append(stateWhenAcquired).append('\'');
    sb.append(", dateAcquired='").append(dateAcquired).append('\'');
    sb.append(", costAmount=").append(costAmount);
    sb.append(", yearStarted=").append(yearStarted);
    sb.append(", isYearStartedGuess=").append(isYearStartedGuess);
    sb.append(", yearDied=").append(yearDied);
    sb.append(", dateSold='").append(dateSold).append('\'');
    sb.append(", soldForAmount=").append(soldForAmount);
    sb.append(", stage='").append(stage).append('\'');
    sb.append(", style='").append(style).append('\'');
    sb.append(", isGrafted=").append(isGrafted);
    sb.append(", isNoHoper='").append(isNoHoper);
    sb.append(", notes='").append(notes).append('\''); // TODO: illegal chars?
    sb.append('}');
    return sb.toString();
  }

}
