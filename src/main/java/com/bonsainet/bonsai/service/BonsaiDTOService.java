package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.repository.BonsaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BonsaiDTOService implements IBonsaiDTOService {

  private final ApplicationContext context;

  // @Autowired
  private final BonsaiRepository repository;

  public BonsaiDTOService(ApplicationContext context, BonsaiRepository repository) {
    this.context = context;
    this.repository = repository;
  }

  @Override
  public List<BonsaiDTO> findAll() {
    return ((List<Bonsai>) repository
        .findAll())
        .stream()
        .map(this::convertToBonsaiDTO)
        .collect(Collectors.toList());
  }

  @Override
  public Page<BonsaiDTO> findAll(Pageable pageable) {
    return repository
        .findAll(pageable)
        .map(this::convertToBonsaiDTO);
  }

  @Override
  public Optional<BonsaiDTO> findById(Integer id) {
    Optional<Bonsai> bonsai = repository.findById(id);
    return bonsai.map(this::convertToBonsaiDTO);
  }

  @Override
  public Page<BonsaiDTO> findByNameContaining(String name, Pageable pageable) {
    return repository
        .findByNameContaining(name, pageable)
        .map(this::convertToBonsaiDTO);
  }

  @Override
  public Long count() {
    return repository.count();
  }

  public BonsaiDTO convertToBonsaiDTO(Bonsai bonsai) {
    IBonsaiMapperImpl iBonsaiMapper = new IBonsaiMapperImpl();
    return iBonsaiMapper.bonsaiToBonsaiDTO(bonsai);
  }

}
