package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.repository.BonsaiRepository;
import com.bonsainet.bonsai.repository.TaxonRepository;
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
  private final TaxonRepository taxonRepository;

  public BonsaiService(ApplicationContext context, BonsaiRepository repository, TaxonRepository taxonRepository) {
    this.context = context;
    this.repository = repository;
    this.taxonRepository = taxonRepository;
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
  public Page<Bonsai> findByNameOrTaxonContaining(String name, Pageable pageable) {
    return repository.findByNameOrTaxonContaining(name, pageable);
  }

  @Override
  public Long count() {
    return repository.count();
  }

  //TODO not a huge fan of this at all
  public Bonsai toBonsai(BonsaiDTO bonsaiDto) {
    IBonsaiMapperImpl iBonsaiMapper = new IBonsaiMapperImpl();
    Bonsai bonsai = iBonsaiMapper.toBonsai(bonsaiDto);
    Optional<Taxon> t = taxonRepository.findById(bonsaiDto.getTaxonId());
    t.ifPresent(bonsai::setTaxon);
    return bonsai;
  }

  public BonsaiDTO toDto(Bonsai bonsai) {
    IBonsaiMapperImpl iBonsaiMapper = new IBonsaiMapperImpl();
    BonsaiDTO bonsaiDTO = iBonsaiMapper.toDTO(bonsai);
    bonsaiDTO.setTaxonId(bonsai.getTaxon().getId());
    return bonsaiDTO;
  }
}
