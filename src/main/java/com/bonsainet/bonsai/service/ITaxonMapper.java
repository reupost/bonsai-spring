package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.model.TaxonDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ITaxonMapper {

  //TODO is there a way of using the @Mapping to convert taxon <--> taxonId instead of doing it manually in BonsaiService?
  TaxonDTO toDTO(Taxon taxon);

  Taxon toTaxon(TaxonDTO taxonDto);
}

