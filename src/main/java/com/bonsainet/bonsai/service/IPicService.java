package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Pic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface IPicService {

  List<Pic> findAll();

  Page<Pic> findAll(Pageable pageable);

  Pic save(Pic p) throws InterruptedException;

  void delete(Pic p);

  Optional<Pic> findById(Integer id);

  Page<Pic> findByEntityTypeAndEntityId(String entityType, Integer entityId, Pageable pageable);

  Page<Pic> findByEntityType(String entityType, Pageable pageable);

  Page<Pic> findByEntityTypeAndTitleContaining(String entityType, String title, Pageable pageable);

  Page<Pic> findByEntityTypeAndEntityIdAndTitleContaining(String entityType, Integer entityId,
      String title, Pageable pageable);

  Page<Pic> findByTitleContaining(String title, Pageable pageable);

  Long count();
}