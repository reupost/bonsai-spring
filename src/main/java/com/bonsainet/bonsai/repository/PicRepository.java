package com.bonsainet.bonsai.repository;

import com.bonsainet.bonsai.model.Pic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PicRepository extends PagingAndSortingRepository<Pic, Integer> {

  Page<Pic> findByEntityTypeAndEntityId(String entityType, Integer entityId, Pageable pageable);

  Page<Pic> findByEntityType(String entityType, Pageable pageable);

  Page<Pic> findByEntityTypeAndTitleContaining(String entityType, String title, Pageable pageable);

  Page<Pic> findByEntityTypeAndEntityIdAndTitleContaining(String entityType, Integer entityId,
      String title, Pageable pageable);

  Page<Pic> findByTitleContaining(String title, Pageable pageable);
}
