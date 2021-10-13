package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface IBonsaiMapper {

  @Named("taxonToTaxonId")
  static Integer taxonToTaxonId(Taxon taxon) {
    if (taxon != null) {
      return taxon.getId();
    } else {
      return null;
    }
  }

  @Named("userToUserId")
  static Integer userToUserId(User user) {
    if (user != null) {
      return user.getId();
    } else {
      return null;
    }
  }

//  @Named("taxonIdToTaxon")
//  public static Taxon taxonIdToTaxon(Integer taxonId) {
//    Optional<Taxon> t = taxonRepository.findById(taxonId);
//    if (taxon != null) {
//      return t.get();
//    } else {
//      return null;
//    }
//  }


  //TODO is there a way of using the @Mapping to convert taxon <--> taxonId instead of doing it manually in BonsaiService?
  @Mapping(source = "taxon", target = "taxonId", qualifiedByName = "taxonToTaxonId")
  @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
  BonsaiDTO toDTO(Bonsai bonsai);

//  @Mapping(source = "taxonId", target = "taxon", qualifiedByName = "taxonIdToTaxon")
//  @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
  Bonsai toBonsai(BonsaiDTO bonsaiDto);
}
