package com.bonsainet.taxon.service;

import com.bonsainet.taxon.model.Bonsai;
import com.bonsainet.taxon.model.BonsaiTaxonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBonsaiTaxonDTOService {

    List<BonsaiTaxonDTO> findAll();

    Page<BonsaiTaxonDTO> findAll(Pageable pageable);

    Optional<BonsaiTaxonDTO> findById(Integer bonsaiId);

    Page<BonsaiTaxonDTO> findByNameContaining(String name, Pageable pageable);

    Long count();

    BonsaiTaxonDTO convertToBonsaiTaxonDTO(Bonsai bonsai);
}
