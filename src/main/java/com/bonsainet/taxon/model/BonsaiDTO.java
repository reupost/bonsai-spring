package com.bonsainet.taxon.model;

import java.util.Date;
import lombok.Data;

@Data
public class BonsaiDTO {

  private Integer bonsaiId;
  private Integer taxonId;
  private String taxonFullName;
  private String taxonFamily;
  private String taxonGenus;
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

  public BonsaiDTO() {
  }

  public BonsaiDTO(Integer bonsaiId, Integer taxonId, Integer tag) {
    this.bonsaiId = bonsaiId;
    this.taxonId = taxonId;
    this.tag = tag;
  }

  /* @Override
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
  } */

}
