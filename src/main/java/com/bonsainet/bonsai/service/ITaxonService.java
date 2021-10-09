package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.model.TaxonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ITaxonService {

  List<Taxon> findAll();

  Page<Taxon> findAll(Pageable pageable);

  Taxon save(Taxon t);

  void delete(Taxon t);

  Optional<Taxon> findById(Integer id);

  Page<Taxon> findByFullNameContaining(String search, Pageable pageable);

  Page<Taxon> findByFullNameOrCommonNameContaining(String search, Pageable pageable);

  Long count();

  Taxon toTaxon(TaxonDTO taxonDto);

  TaxonDTO toDto(Taxon taxon);
}
