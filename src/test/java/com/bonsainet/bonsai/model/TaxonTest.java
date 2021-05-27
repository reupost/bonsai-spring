package com.bonsainet.bonsai.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TaxonTest {

  @Test
  public void composeFullNameAllPartsTest() {
    Taxon taxon = new Taxon(1, "family", "genus", "species", "cultivar",
        "commonName", "generalType", 1);

    String fullName = taxon.getFullName();
    assertEquals(fullName, "genus species 'cultivar'");
  }

  @Test
  public void composeFullNameGenusSpeciesTest() {
    Taxon taxon = new Taxon(1, "family", "genus", "species", null,
        "commonName", "generalType", 1);

    String fullName = taxon.getFullName();
    assertEquals(fullName, "genus species");
  }

  @Test
  public void composeFullNameGenusTest() {
    Taxon taxon = new Taxon(1, "family", "genus", null, null,
        "commonName", "generalType", 1);

    String fullName = taxon.getFullName();
    assertEquals(fullName, "genus sp.");
  }

  @Test
  public void composeFullNameTrimTest() {
    Taxon taxon = new Taxon(1, "family", " genus ", " species ", " cultivar ",
        "commonName", "generalType", 1);

    String fullName = taxon.getFullName();
    assertEquals(fullName, "genus species 'cultivar'");
  }
}
