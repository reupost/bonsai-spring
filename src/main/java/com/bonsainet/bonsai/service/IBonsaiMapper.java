package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IBonsaiMapper {

  @Mapping(target="taxon", source = "taxon")
  BonsaiDTO bonsaiToBonsaiDTO(Bonsai bonsai);
}
