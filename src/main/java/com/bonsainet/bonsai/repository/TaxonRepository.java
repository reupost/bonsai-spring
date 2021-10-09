package com.bonsainet.bonsai.repository;

import com.bonsainet.bonsai.model.Taxon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface TaxonRepository extends PagingAndSortingRepository<Taxon, Integer> {

  Page<Taxon> findByFullNameContaining(String fullname, Pageable pageable);

  @Query("SELECT t FROM Taxon t WHERE (t.fullName LIKE CONCAT('%',:filter,'%') OR t.commonName LIKE CONCAT('%',:filter,'%'))")
  Page<Taxon> findByFullNameOrCommonNameContaining(@Param("filter") String filter, Pageable pageable);
}
