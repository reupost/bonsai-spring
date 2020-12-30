package com.bonsainet.species.repository;

import com.bonsainet.species.model.Taxon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface TaxonRepository extends PagingAndSortingRepository<Taxon, Integer> {

    Page<Taxon> findByFullNameContaining(String fullname, Pageable pageable);
}
