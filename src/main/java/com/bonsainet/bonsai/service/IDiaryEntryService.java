package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.DiaryEntry;
import com.bonsainet.bonsai.model.DiaryEntryDTO;
import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.model.TaxonDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDiaryEntryService {

  List<DiaryEntry> findAll();

  Page<DiaryEntry> findAll(Pageable pageable);

  DiaryEntry save(DiaryEntry b);

  void delete(DiaryEntry b);

  Optional<DiaryEntry> findById(Integer id);

  Page<DiaryEntry> findByBonsai(Bonsai bonsai, Pageable pageable);

  Page<DiaryEntry> findByEntryTextContaining(String text, Pageable pageable);

  Page<DiaryEntry> findByEntryTextOrBonsaiContaining(String text, Pageable pageable);

  Long count();

  DiaryEntry toDiaryEntry(DiaryEntryDTO diaryEntryDto);

  DiaryEntryDTO toDto(DiaryEntry diaryEntry);
}
