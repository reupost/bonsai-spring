package com.bonsainet.bonsai.repository;

import com.bonsainet.bonsai.model.Bonsai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface BonsaiRepository extends PagingAndSortingRepository<Bonsai, Integer> {

  Page<Bonsai> findByNameContaining(String name, Pageable pageable);

  //@Query("SELECT b FROM Bonsai b, Taxon t WHERE b.taxon = t AND (b.name LIKE '%:filter%' OR t.fullName LIKE '%:filter%' OR t.commonName LIKE '%:filter%')")
  @Query(value = "SELECT b.* FROM bonsai b LEFT JOIN taxon t ON b.taxon_id = t.id WHERE (b.name LIKE %:filter% OR t.full_name LIKE %:filter% OR t.common_name LIKE %:filter%)",
      nativeQuery = true)
  Page<Bonsai> findByNameOrTaxonContaining(@Param("filter") String filter, Pageable pageable);
}
