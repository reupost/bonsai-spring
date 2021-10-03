package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.model.PicDTO;
import org.mapstruct.Mapper;

@Mapper
public interface IPicMapper {
  //TODO is there a way of using the @Mapping to convert taxon <--> taxonId instead of doing it manually in BonsaiService?
  PicDTO toDTO(Pic pic);

  Pic toPic(PicDTO picDto);
}

