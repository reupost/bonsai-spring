package com.bonsainet.bonsai.repository;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.DiaryEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DiaryEntryRepository extends PagingAndSortingRepository<DiaryEntry, Integer> {

  Page<DiaryEntry> findByBonsai(Bonsai bonsai, Pageable pageable);

  Page<DiaryEntry> findByEntryTextContaining(String text, Pageable pageable);

}
