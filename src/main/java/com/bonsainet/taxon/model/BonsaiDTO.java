package com.bonsainet.taxon.model;

import java.util.Date;

public class BonsaiDTO {

  public Integer bonsaiId;
  public Integer taxonId;
  public String taxonFullName;
  public String taxonFamily;
  public String taxonGenus;
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

  public BonsaiDTO() {
  }

  public BonsaiDTO(Integer bonsaiId, Integer taxonId, Integer tag) {
    this.bonsaiId = bonsaiId;
    this.taxonId = taxonId;
    this.tag = tag;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("BonsaiTaxonDTO{");
    sb.append("bonsaiId=").append(bonsaiId);
    sb.append(", taxonId=").append(taxonId);
    sb.append(", taxonFullName='").append(taxonFullName).append('\'');
    sb.append(", taxonFamily='").append(taxonFamily).append('\'');
    sb.append(", taxonGenus='").append(taxonGenus).append('\'');
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
    sb.append(", isNoHoper=").append(isNoHoper);
    sb.append(", notes='").append(notes).append('\''); // TODO: illegal chars?
    sb.append('}');
    return sb.toString();
  }

}
