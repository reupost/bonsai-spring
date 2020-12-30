package com.bonsainet.species.repository;

import com.bonsainet.species.model.Taxon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface TaxonRepository extends PagingAndSortingRepository<Taxon, Integer> {

    Page<Taxon> findByFullNameContaining(String fullname, Pageable pageable);
}
