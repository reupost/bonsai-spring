package com.bonsainet.taxon.service;

import com.bonsainet.taxon.model.Bonsai;
import com.bonsainet.taxon.model.BonsaiDTO;
import com.bonsainet.taxon.model.Taxon;
import com.bonsainet.taxon.repository.BonsaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BonsaiDTOService implements IBonsaiDTOService {

  @Autowired
  private BonsaiRepository repository;

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
    BonsaiDTO bonsaiDTO = new BonsaiDTO();
    bonsaiDTO.bonsaiId = bonsai.id;
    bonsaiDTO.costAmount = bonsai.costAmount;
    bonsaiDTO.tag = bonsai.tag;
    bonsaiDTO.dateAcquired = bonsai.dateAcquired;
    bonsaiDTO.dateSold = bonsai.dateSold;
    bonsaiDTO.isGrafted = bonsai.isGrafted;
    bonsaiDTO.isNoHoper = bonsai.isNoHoper;
    bonsaiDTO.isYearStartedGuess = bonsai.isYearStartedGuess;
    bonsaiDTO.name = bonsai.name;
    bonsaiDTO.notes = bonsai.notes;
    bonsaiDTO.numberOfPlants = bonsai.numberOfPlants;
    bonsaiDTO.soldForAmount = bonsai.soldForAmount;
    bonsaiDTO.source = bonsai.source;
    bonsaiDTO.stage = bonsai.stage;
    bonsaiDTO.stateWhenAcquired = bonsai.stateWhenAcquired;
    bonsaiDTO.style = bonsai.style;
    bonsaiDTO.yearDied = bonsai.yearDied;
    bonsaiDTO.yearStarted = bonsai.yearStarted;

    Taxon taxon = bonsai.getTaxon();
    bonsaiDTO.taxonId = taxon.id;
    bonsaiDTO.taxonFamily = taxon.family;
    bonsaiDTO.taxonGenus = taxon.genus;
    bonsaiDTO.taxonFullName = taxon.fullName;

    return bonsaiDTO;
  }
}
