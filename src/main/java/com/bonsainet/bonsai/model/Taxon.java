package com.bonsainet.bonsai.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "taxon")
@Data
public class Taxon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String family;
  private String genus;
  private String species;
  private String cultivar;
  private String commonName;
  private String generalType;
  private Integer countBonsais;
  private String fullName;

  public Taxon() {
  }

  public Taxon(Integer id, String family, String genus, String species, String cultivar,
      String commonName, String generalType, Integer countBonsais) {
    this.id = id;
    this.commonName = commonName;
    this.cultivar = cultivar;
    this.family = family;
    this.generalType = generalType;
    this.species = species;
    this.genus = genus;
    this.countBonsais = countBonsais;

    composeFullName();

  }

  public boolean composeFullName() {
    String genusNotNull = Objects.toString(this.genus, "").trim();
    String speciesNotNull = Objects.toString(this.species, "sp.").trim();
    String cultivarNotNull = Objects.toString(this.cultivar, "").trim();
    this.fullName = (genusNotNull + " " + speciesNotNull +
        (!cultivarNotNull.equals("") ? " '" + cultivarNotNull + "'" : "")).trim();
    return true;
  }

}
