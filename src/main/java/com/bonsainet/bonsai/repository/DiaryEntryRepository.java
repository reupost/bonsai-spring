package com.bonsainet.bonsai.repository;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.DiaryEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface DiaryEntryRepository extends PagingAndSortingRepository<DiaryEntry, Integer> {

  Page<DiaryEntry> findByBonsai(Bonsai bonsai, Pageable pageable);

  Page<DiaryEntry> findByEntryTextContaining(String text, Pageable pageable);

  @Query("SELECT d FROM DiaryEntry d, Bonsai b, Taxon t WHERE d.bonsai = b AND b.taxon = t AND "
      + "(d.entryText LIKE CONCAT('%',:filter,'%') OR "
      + "t.fullName LIKE CONCAT('%',:filter,'%') OR t.commonName LIKE CONCAT('%',:filter,'%'))")
  Page<DiaryEntry> findByEntryTextOrBonsaiContaining(@Param("filter") String filter, Pageable pageable);

}
