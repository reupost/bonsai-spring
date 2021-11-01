package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface IBonsaiService {

  List<Bonsai> findAll();

  Page<Bonsai> findAll(Pageable pageable);

  List<Bonsai> findAll(User user);

  Page<Bonsai> findAll(User user, Pageable pageable);

  Bonsai save(Bonsai b);

  void delete(Bonsai b);

  Optional<Bonsai> findById(Integer id);

  Page<Bonsai> findByTaxon(Taxon taxon, Pageable pageable);

  Page<Bonsai> findByTaxon(User user, Taxon taxon, Pageable pageable);

  Page<Bonsai> findByNameContaining(String name, Pageable pageable);

  Page<Bonsai> findByNameContaining(User user, String name, Pageable pageable);

  Page<Bonsai> findByNameOrTaxonContaining(String name, Pageable pageable);

  Page<Bonsai> findByNameOrTaxonContaining(User user, String name, Pageable pageable);

  Long count();

  Long count(User user);

  Bonsai toBonsai(BonsaiDTO bonsaiDto);

  BonsaiDTO toDto(Bonsai bonsai);

}
