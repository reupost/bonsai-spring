package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.repository.PicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Service
public class PicService implements IPicService {

  private static final Logger logger = LoggerFactory.getLogger(PicService.class);

  @Value("${pic.rootfolder}")
  private String picRootFolder;

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
    return repository.findAll(pageable);
  }

  @Override
  public Future<Pic> save(Pic p) throws InterruptedException {
    CompletableFuture<Pic> completableFuture = new CompletableFuture<>();

    Executors.newCachedThreadPool().submit(() -> {
      p.setRootFolder(this.picRootFolder);
      p.setDimensions();
      p.setThumb();
      Pic pSaved = repository.save(p);
      completableFuture.complete(pSaved);
    });

    return completableFuture;
  }

  @Override
  public void delete(Pic p) {
    repository.delete(p);
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
