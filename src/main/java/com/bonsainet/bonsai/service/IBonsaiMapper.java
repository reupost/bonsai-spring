package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IBonsaiMapper {

  //TODO is there a way of using the @Mapping to convert taxon <--> taxonId instead of doing it manually in BonsaiService?
  BonsaiDTO toDTO(Bonsai bonsai);

  Bonsai toBonsai(BonsaiDTO bonsaiDto);
}
