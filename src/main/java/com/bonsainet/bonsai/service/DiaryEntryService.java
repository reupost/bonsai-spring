package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.DiaryEntry;
import com.bonsainet.bonsai.repository.DiaryEntryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DiaryEntryService implements IDiaryEntryService {

  private final ApplicationContext context;

  private final DiaryEntryRepository repository;

  public DiaryEntryService(ApplicationContext context, DiaryEntryRepository repository) {
    this.context = context;
    this.repository = repository;
  }

  @Override
  public List<DiaryEntry> findAll() {
    return (List<DiaryEntry>) repository.findAll();
  }

  @Override
  public Page<DiaryEntry> findAll(Pageable pageable) {
    return (Page<DiaryEntry>) repository.findAll(pageable);
  }

  @Override
  public DiaryEntry save(DiaryEntry b) {
    return repository.save(b);
  }

  @Override
  public void delete(DiaryEntry t) {
    repository.delete(t);
  }

  @Override
  public Optional<DiaryEntry> findById(Integer id) {
    return repository.findById(id);
  }

  @Override
  public Page<DiaryEntry> findByBonsai(Bonsai bonsai, Pageable pageable) {
    return repository.findByBonsai(bonsai, pageable);
  }

  @Override
  public Page<DiaryEntry> findByEntryTextContaining(String text, Pageable pageable) {
    return repository.findByEntryTextContaining(text, pageable);
  }

  @Override
  public Long count() {
    return repository.count();
  }

}
