package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface IBonsaiService {

  List<Bonsai> findAll();


  Page<Bonsai> findAll(Pageable pageable);

  Bonsai save(Bonsai b);

  void delete(Bonsai b);

  Optional<Bonsai> findById(Integer id);

  Page<Bonsai> findByNameContaining(String name, Pageable pageable);

  Long count();
}