package com.bonsainet.bonsai.model;

import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

@Data
public class BonsaiDTO {

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

  private Integer taxonId;

  private Integer userId;

  public BonsaiDTO() {
  }

}
