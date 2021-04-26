package com.bonsainet.taxon.repository;

import com.bonsainet.taxon.model.Taxon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface TaxonRepository extends PagingAndSortingRepository<Taxon, Integer> {

  Page<Taxon> findByFullNameContaining(String fullname, Pageable pageable);
}
