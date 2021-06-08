package com.bonsainet.bonsai.model;

import java.util.Date;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class BonsaiDTO {

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

  private TaxonDTO taxon;

  private Page<Pic> pics;

  public BonsaiDTO() {
  }

}
