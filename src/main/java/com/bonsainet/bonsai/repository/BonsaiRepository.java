package com.bonsainet.bonsai.repository;

import com.bonsainet.bonsai.model.Bonsai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface BonsaiRepository extends PagingAndSortingRepository<Bonsai, Integer> {

  Page<Bonsai> findByNameContaining(String name, Pageable pageable);
}
