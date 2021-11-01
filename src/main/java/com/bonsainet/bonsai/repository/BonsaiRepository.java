package com.bonsainet.bonsai.repository;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.model.User;
import java.util.List;
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

  @Query("SELECT b FROM Bonsai b, Taxon t WHERE b.taxon = t AND b.user = :user AND "
      + "(b.name LIKE CONCAT('%',:filter,'%') "
      + "OR t.fullName LIKE CONCAT('%',:filter,'%') OR t.commonName LIKE CONCAT('%',:filter,'%'))")
  Page<Bonsai> findByUserAndNameOrTaxonContaining(@Param("user") User user, @Param("filter") String filter, Pageable pageable);

  Page<Bonsai> findByUserAndNameContaining(User user, String name, Pageable pageable);

  Page<Bonsai> findByUser(User user, Pageable pageable);

  List<Bonsai> findByUser(User user);

  Page<Bonsai> findByTaxon(Taxon taxon, Pageable pageable);

  Page<Bonsai> findByUserAndTaxon(User user, Taxon taxon, Pageable pageable);

  Long countByUser(User user);
}
