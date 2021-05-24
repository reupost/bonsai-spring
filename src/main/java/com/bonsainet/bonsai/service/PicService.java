package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.repository.PicRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PicService implements IPicService {

  private final ApplicationContext context;

  private final PicRepository repository;

  public PicService(ApplicationContext context, PicRepository repository) {
    this.context = context;
    this.repository = repository;
  }

  @Override
  public List<Pic> findAll() {
    return (List<Pic>) repository.findAll();
  }

  @Override
  public Page<Pic> findAll(Pageable pageable) {
    return (Page<Pic>) repository.findAll(pageable);
  }


  @Override
  public Pic save(Pic b) {
    return repository.save(b);
  }

  @Override
  public void delete(Pic t) {
    repository.delete(t);
  }

  @Override
  public Optional<Pic> findById(Integer id) {
    return repository.findById(id);
  }

  @Override
  public Page<Pic> findByEntityTypeAndEntityId(String entityType, Integer entityId,
      Pageable pageable) {
    return repository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
  }

  @Override
  public Page<Pic> findByEntityType(String entityType, Pageable pageable) {
    return repository.findByEntityType(entityType, pageable);
  }

  @Override
  public Page<Pic> findByEntityTypeAndTitleContaining(String entityType, String title,
      Pageable pageable) {
    return repository.findByEntityTypeAndTitleContaining(entityType, title, pageable);
  }

  @Override
  public Page<Pic> findByEntityTypeAndEntityIdAndTitleContaining(String entityType,
      Integer entityId, String title, Pageable pageable) {
    return repository
        .findByEntityTypeAndEntityIdAndTitleContaining(entityType, entityId, title, pageable);
  }

  @Override
  public Page<Pic> findByTitleContaining(String title, Pageable pageable) {
    return repository.findByTitleContaining(title, pageable);
  }

  @Override
  public Long count() {
    return repository.count();
  }
}
