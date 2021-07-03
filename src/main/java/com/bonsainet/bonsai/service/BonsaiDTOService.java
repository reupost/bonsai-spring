package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.repository.BonsaiRepository;
import com.bonsainet.bonsai.repository.PicRepository;
import java.util.ArrayList;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BonsaiDTOService implements IBonsaiDTOService {

  private final ApplicationContext context;

  private final BonsaiRepository repository;
  private final PicRepository picRepository;

  public BonsaiDTOService(ApplicationContext context,
      BonsaiRepository repository, PicRepository picRepository) {
    this.context = context;
    this.repository = repository;
    this.picRepository = picRepository;
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
    return bonsai.map(b -> convertToBonsaiDTO(b, true));
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
    return convertToBonsaiDTO(bonsai, true);
  }

  public BonsaiDTO convertToBonsaiDTO(Bonsai bonsai, boolean withPics) {
    IBonsaiMapperImpl iBonsaiMapper = new IBonsaiMapperImpl();
    BonsaiDTO bonsaiDTO = iBonsaiMapper.bonsaiToBonsaiDTO(bonsai);
    if (withPics) {
      ArrayList<Order> sortBy = new ArrayList<>();
      sortBy.add(new Sort.Order(Sort.Direction.DESC, "id"));
      Sort sortFinal = Sort.by(sortBy);

      Pageable paging = PageRequest.of(0, 1, sortFinal);

      Page<Pic> pics = picRepository.findByEntityTypeAndEntityId("bonsai", bonsai.getId(), paging);
      if (pics != null && pics.hasContent()) {
        bonsaiDTO.setPics(pics);
      }
    }
    return bonsaiDTO;
  }

}
