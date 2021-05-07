package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.repository.TaxonRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaxonService implements ITaxonService {

  private final ApplicationContext context;

  // @Autowired
  private TaxonRepository repository;

  public TaxonService(ApplicationContext context, TaxonRepository repository) {
    this.context = context;
    this.repository = repository;
  }

  @Override
  public List<Taxon> findAll() {
    return (List<Taxon>) repository.findAll();
  }

  @Override
  public Page<Taxon> findAll(Pageable pageable) {
    return (Page<Taxon>) repository.findAll(pageable);
  }


  @Override
  public Taxon save(Taxon t) {
    try {
      t.composeFullName();
      return repository.save(t);
    } catch (NullPointerException npe) {
      throw new IllegalArgumentException("invalid taxon");
    }
  }

  @Override
  public void delete(Taxon t) {
    repository.delete(t);
  }

  @Override
  public Optional<Taxon> findById(Integer id) {
    return repository.findById(id);
  }

  @Override
  public Page<Taxon> findByFullNameContaining(String fullname, Pageable pageable) {
    return repository.findByFullNameContaining(fullname, pageable);
  }

  @Override
  public Long count() {
    return repository.count();
  }


}
