package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.EntityTypes;
import com.bonsainet.bonsai.model.Pic;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import org.springframework.web.multipart.MultipartFile;

public interface IPicService {

  List<Pic> findAll();

  Page<Pic> findAll(Pageable pageable);

  Future<Pic> save(Pic p);

  void delete(Pic p);

  public String storeFile(MultipartFile file);

  public Resource loadFileAsResource(String fileName) throws Exception;

  Optional<Pic> findById(Integer id);

  Page<Pic> findByEntityTypeAndEntityId(EntityTypes entityType, Integer entityId, Pageable pageable);

  Page<Pic> findByEntityType(EntityTypes entityType, Pageable pageable);

  Page<Pic> findByEntityTypeAndTitleContaining(EntityTypes entityType, String title, Pageable pageable);

  Page<Pic> findByEntityTypeAndEntityIdAndTitleContaining(EntityTypes entityType, Integer entityId,
      String title, Pageable pageable);

  Page<Pic> findByTitleContaining(String title, Pageable pageable);

  Long count();
}
