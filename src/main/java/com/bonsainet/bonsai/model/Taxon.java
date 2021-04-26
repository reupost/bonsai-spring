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

  private Taxon() {
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
    this.fullName = Objects.toString(this.genus, "") +
        (!Objects.toString(this.species, "").equals("") ? " " + Objects.toString(this.species, "")
            : "") +
        (!Objects.toString(this.cultivar, "").equals("") ? " '" + Objects
            .toString(this.cultivar, "") + "'" : "");
    return true;
  }

}
