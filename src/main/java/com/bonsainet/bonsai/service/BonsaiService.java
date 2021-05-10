package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.repository.BonsaiRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BonsaiService implements IBonsaiService {

  private final ApplicationContext context;

  private final BonsaiRepository repository;

  public BonsaiService(ApplicationContext context, BonsaiRepository repository) {
    this.context = context;
    this.repository = repository;
  }

  @Override
  public List<Bonsai> findAll() {
    return (List<Bonsai>) repository.findAll();
  }

  @Override
  public Page<Bonsai> findAll(Pageable pageable) {
    return (Page<Bonsai>) repository.findAll(pageable);
  }


  @Override
  public Bonsai save(Bonsai b) {
    return repository.save(b);
  }

  @Override
  public void delete(Bonsai t) {
    repository.delete(t);
  }

  @Override
  public Optional<Bonsai> findById(Integer id) {
    return repository.findById(id);
  }

  @Override
  public Page<Bonsai> findByNameContaining(String name, Pageable pageable) {
    return repository.findByNameContaining(name, pageable);
  }

  @Override
  public Long count() {
    return repository.count();
  }
}
