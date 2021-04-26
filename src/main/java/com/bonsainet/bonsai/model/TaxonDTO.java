package com.bonsainet.bonsai.model;

import lombok.Data;


@Data
public class TaxonDTO {

  private Integer id;

  private String family;
  private String genus;
  private String species;
  private String cultivar;
  private String commonName;
  private String generalType;
  private Integer countBonsais;
  private String fullName;
  // TODO omit unneeded properties

  public TaxonDTO() {
  }

  public TaxonDTO(Integer taxonId) {
    this.id = taxonId;
  }
}
