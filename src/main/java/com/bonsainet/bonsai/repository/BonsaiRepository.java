package com.bonsainet.bonsai.repository;

import com.bonsainet.bonsai.model.Bonsai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface BonsaiRepository extends PagingAndSortingRepository<Bonsai, Integer> {

  Page<Bonsai> findByNameContaining(String name, Pageable pageable);

  @Query("SELECT b FROM Bonsai b, Taxon t WHERE b.taxon = t AND (b.name LIKE CONCAT('%',:filter,'%') "
      + "OR t.fullName LIKE CONCAT('%',:filter,'%') OR t.commonName LIKE CONCAT('%',:filter,'%'))")
  Page<Bonsai> findByNameOrTaxonContaining(@Param("filter") String filter, Pageable pageable);
}
