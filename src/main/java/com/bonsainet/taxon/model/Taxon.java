package com.bonsainet.taxon.model;

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

  /* @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Taxon{");
    sb.append("id=").append(id);
    sb.append(",family='").append(family).append('\'');
    sb.append(", genus='").append(genus).append('\'');
    sb.append(", species='").append(species).append('\'');
    sb.append(", cultivar='").append(cultivar).append('\'');
    sb.append(", commonName='").append(commonName).append('\'');
    sb.append(", fullName='").append(fullName).append('\'');
    sb.append(", generalType='").append(generalType).append('\'');
    sb.append(", countBonsais=").append(countBonsais);
    sb.append('}');
    return sb.toString();
  } */

}
