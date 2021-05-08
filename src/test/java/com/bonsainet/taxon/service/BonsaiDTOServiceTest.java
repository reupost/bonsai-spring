package com.bonsainet.taxon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.repository.BonsaiRepository;
import com.bonsainet.bonsai.service.BonsaiDTOService;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

class BonsaiDTOServiceTest {

  private BonsaiDTOService bonsaiDtoService;
  private ApplicationContext applicationContext;

  BonsaiRepository bonsaiRepository;

  @BeforeEach
  void setup() {
    applicationContext = mock(ApplicationContext.class);
    bonsaiRepository = mock(BonsaiRepository.class);
    bonsaiDtoService = new BonsaiDTOService(applicationContext, bonsaiRepository);

  }

  @Test
  void shouldFindAllBonsaiDTOs() {
    bonsaiDtoService.findAll();

    verify(bonsaiRepository).findAll();
    verifyNoMoreInteractions(bonsaiRepository);
  }

  @Test
  void shouldFindAllBonsaiDTOsPageable() {
    int bonsaiId = 1;
    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);
    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    bonsaiList.add(bonsai);

    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);
    PageRequest pageRequest = PageRequest.of(0, 1, sortFinal);
    Page<Bonsai> pageBonsai = new PageImpl<>(bonsaiList, pageRequest, bonsaiList.size());

    when(bonsaiRepository.findAll(paging)).thenReturn(pageBonsai);

    Page<BonsaiDTO> bonsaiDtoReturned = bonsaiDtoService.findAll(paging);

    verify(bonsaiRepository).findAll(paging);
    verifyNoMoreInteractions(bonsaiRepository);
    //TODO: check the Dto returned assertEquals(bonsaiDtoReturned, );
  }

  @Test
  void shouldFindByIdBonsaiDTO() {
    bonsaiDtoService.findById(1);
    verify(bonsaiRepository).findById(1);
    verifyNoMoreInteractions(bonsaiRepository);
  }

  @Test
  void shouldCountBonsaiDTO() {
    bonsaiDtoService.count();
    verify(bonsaiRepository).count();
    verifyNoMoreInteractions(bonsaiRepository);
  }

  @Test
  void shouldFindDtoByNameContaining() {
    int bonsaiId = 1;
    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);
    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    bonsaiList.add(bonsai);

    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);
    PageRequest pageRequest = PageRequest.of(0, 1, sortFinal);
    Page<Bonsai> pageBonsai = new PageImpl<>(bonsaiList, pageRequest, bonsaiList.size());

    when(bonsaiRepository.findByNameContaining("test", paging)).thenReturn(pageBonsai);

    bonsaiDtoService.findByNameContaining("test", paging);

    verify(bonsaiRepository).findByNameContaining("test", paging);
    verifyNoMoreInteractions(bonsaiRepository);
    //TODO check dto?
  }

  @Test
  void shouldConvertBonsaiToDTO() {
    int bonsaiId = 1;
    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);

    BonsaiDTO bonsaiDto = new BonsaiDTO();
    bonsaiDto.setId(bonsaiId);

    BonsaiDTO bonsaiDtoReturned = bonsaiDtoService.convertToBonsaiDTO(bonsai);

    assertEquals(bonsaiDtoReturned, bonsaiDto);
  }

}
