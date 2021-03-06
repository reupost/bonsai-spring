package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.repository.BonsaiRepository;
import com.bonsainet.bonsai.repository.TaxonRepository;
import com.bonsainet.bonsai.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BonsaiService implements IBonsaiService {

  private final ApplicationContext context;

  private final BonsaiRepository repository;
  private final TaxonRepository taxonRepository;
  private final UserRepository userRepository;

  public BonsaiService(ApplicationContext context, BonsaiRepository repository,
      TaxonRepository taxonRepository, UserRepository userRepository) {
    this.context = context;
    this.repository = repository;
    this.taxonRepository = taxonRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<Bonsai> findAll() {
    return (List<Bonsai>) repository.findAll();
  }

  @Override
  public Page<Bonsai> findAll(Specification<Bonsai> bonsaiSpecification, Pageable pageable) {
    return repository.findAll(bonsaiSpecification, pageable);
  }

  @Override
  public Page<Bonsai> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public List<Bonsai> findAll(User user) {
    return repository.findByUser(user);
  }

  @Override
  public Page<Bonsai> findAll(User user, Pageable pageable) {
    return repository.findByUser(user, pageable);
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
  public Page<Bonsai> findByTaxon(Taxon taxon, Pageable pageable) {
    return repository.findByTaxon(taxon, pageable);
  }

  @Override
  public Page<Bonsai> findByTaxon(User user, Taxon taxon, Pageable pageable) {
    return repository.findByUserAndTaxon(user, taxon, pageable);
  }

  @Override
  public Page<Bonsai> findByNameContaining(String name, Pageable pageable) {
    return repository.findByNameContaining(name, pageable);
  }
  @Override
  public Page<Bonsai> findByNameOrTaxonContaining(String name, Pageable pageable) {
    return repository.findByNameOrTaxonContaining(name, pageable);
  }

  @Override
  public Page<Bonsai> findByNameContaining(User user, String name, Pageable pageable) {
    return repository.findByUserAndNameContaining(user, name, pageable);
  }

  @Override
  public Page<Bonsai> findByNameOrTaxonContaining(User user, String name, Pageable pageable) {
    return repository.findByUserAndNameOrTaxonContaining(user, name, pageable);
  }

  @Override
  public Long count() {
    return repository.count();
  }

  @Override
  public Long count(User user) {
    return repository.countByUser(user);
  }

  //TODO not a huge fan of this at all
  public Bonsai toBonsai(BonsaiDTO bonsaiDto) {
    IBonsaiMapperImpl iBonsaiMapper = new IBonsaiMapperImpl();
    Bonsai bonsai = iBonsaiMapper.toBonsai(bonsaiDto);
    Optional<Taxon> t = taxonRepository.findById(bonsaiDto.getTaxonId());
    t.ifPresent(bonsai::setTaxon);
    Optional<User> u = userRepository.findById(bonsaiDto.getUserId());
    u.ifPresent(bonsai::setUser);
    return bonsai;
  }

  public BonsaiDTO toDto(Bonsai bonsai) {
    IBonsaiMapperImpl iBonsaiMapper = new IBonsaiMapperImpl();
    BonsaiDTO bonsaiDTO = iBonsaiMapper.toDTO(bonsai);
    return bonsaiDTO;
  }
}
