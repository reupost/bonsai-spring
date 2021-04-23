package com.bonsainet.taxon.service;

import com.bonsainet.taxon.model.Bonsai;
import com.bonsainet.taxon.model.BonsaiTaxonDTO;
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
public class BonsaiTaxonDTOService implements IBonsaiTaxonDTOService {

    @Autowired
    private BonsaiRepository repository;

    @Override
    public List<BonsaiTaxonDTO> findAll() {
        return ((List<Bonsai>) repository
                .findAll())
                .stream()
                .map(this::convertToBonsaiTaxonDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BonsaiTaxonDTO> findAll(Pageable pageable) {
        return repository
                .findAll(pageable)
                .map(this::convertToBonsaiTaxonDTO);
    }

    @Override
    public Optional<BonsaiTaxonDTO> findById(Integer id) {
        Optional<Bonsai> bonsai = repository.findById(id);
        return bonsai.map(this::convertToBonsaiTaxonDTO);
    }

    @Override
    public Page<BonsaiTaxonDTO> findByNameContaining(String name, Pageable pageable) {
        return  repository
                .findByNameContaining(name, pageable)
                .map(this::convertToBonsaiTaxonDTO);
    }

    @Override
    public Long count() {
        return repository.count();
    }

    public BonsaiTaxonDTO convertToBonsaiTaxonDTO(Bonsai bonsai) {
        BonsaiTaxonDTO bonsaiTaxonDTO = new BonsaiTaxonDTO();
        bonsaiTaxonDTO.bonsaiId = bonsai.id;
        bonsaiTaxonDTO.costAmount = bonsai.costAmount;
        bonsaiTaxonDTO.tag = bonsai.tag;
        bonsaiTaxonDTO.dateAcquired = bonsai.dateAcquired;
        bonsaiTaxonDTO.dateSold = bonsai.dateSold;
        bonsaiTaxonDTO.isGrafted = bonsai.isGrafted;
        bonsaiTaxonDTO.isNoHoper = bonsai.isNoHoper;
        bonsaiTaxonDTO.isYearStartedGuess = bonsai.isYearStartedGuess;
        bonsaiTaxonDTO.name = bonsai.name;
        bonsaiTaxonDTO.notes = bonsai.notes;
        bonsaiTaxonDTO.numberOfPlants = bonsai.numberOfPlants;
        bonsaiTaxonDTO.soldForAmount = bonsai.soldForAmount;
        bonsaiTaxonDTO.source = bonsai.source;
        bonsaiTaxonDTO.stage = bonsai.stage;
        bonsaiTaxonDTO.stateWhenAcquired = bonsai.stateWhenAcquired;
        bonsaiTaxonDTO.style = bonsai.style;
        bonsaiTaxonDTO.yearDied = bonsai.yearDied;
        bonsaiTaxonDTO.yearStarted = bonsai.yearStarted;

        Taxon taxon = bonsai.getTaxon();
        bonsaiTaxonDTO.taxonId = taxon.id;
        bonsaiTaxonDTO.taxonFamily = taxon.family;
        bonsaiTaxonDTO.taxonGenus = taxon.genus;
        bonsaiTaxonDTO.taxonFullName = taxon.fullName;

        return bonsaiTaxonDTO;
    }
}
