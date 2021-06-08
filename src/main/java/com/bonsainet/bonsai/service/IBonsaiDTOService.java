package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBonsaiDTOService {

  List<BonsaiDTO> findAll();

  Page<BonsaiDTO> findAll(Pageable pageable);

  Optional<BonsaiDTO> findById(Integer bonsaiId);

  Page<BonsaiDTO> findByNameContaining(String name, Pageable pageable);

  Long count();

  BonsaiDTO convertToBonsaiDTO(Bonsai bonsai);

  BonsaiDTO convertToBonsaiDTO(Bonsai bonsai, boolean withPics);
}
