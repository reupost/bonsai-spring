package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Taxon;
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

  Page<Taxon> findByFullNameContaining(String fullname, Pageable pageable);

  Long count();
}
