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
    bonsaiDTO.setBonsaiId(bonsai.getId());
    bonsaiDTO.setCostAmount(bonsai.getCostAmount());
    bonsaiDTO.setTag(bonsai.getTag());
    bonsaiDTO.setDateAcquired(bonsai.getDateAcquired());
    bonsaiDTO.setDateSold(bonsai.getDateSold());
    bonsaiDTO.setIsGrafted(bonsai.getIsGrafted());
    bonsaiDTO.setIsNoHoper(bonsai.getIsNoHoper());
    bonsaiDTO.setIsYearStartedGuess(bonsai.getIsYearStartedGuess());
    bonsaiDTO.setName(bonsai.getName());
    bonsaiDTO.setNotes(bonsai.getNotes());
    bonsaiDTO.setNumberOfPlants(bonsai.getNumberOfPlants());
    bonsaiDTO.setSoldForAmount(bonsai.getSoldForAmount());
    bonsaiDTO.setSource(bonsai.getSource());
    bonsaiDTO.setStage(bonsai.getStage());
    bonsaiDTO.setStateWhenAcquired(bonsai.getStateWhenAcquired());
    bonsaiDTO.setStyle(bonsai.getStyle());
    bonsaiDTO.setYearDied(bonsai.getYearDied());
    bonsaiDTO.setYearStarted(bonsai.getYearStarted());

    Taxon taxon = bonsai.getTaxon();
    bonsaiDTO.setTaxonId(taxon.getId());
    bonsaiDTO.setTaxonFamily(taxon.getFamily());
    bonsaiDTO.setTaxonGenus(taxon.getGenus());
    bonsaiDTO.setTaxonFullName(taxon.getFullName());

    return bonsaiDTO;
  }
}
