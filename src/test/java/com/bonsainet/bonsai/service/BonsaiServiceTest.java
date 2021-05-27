package com.bonsainet.bonsai.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.repository.BonsaiRepository;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

class BonsaiServiceTest {

  private BonsaiService bonsaiService;
  private ApplicationContext applicationContext;

  BonsaiRepository bonsaiRepository;

  @BeforeEach
  void setup() {
    applicationContext = mock(ApplicationContext.class);
    bonsaiRepository = mock(BonsaiRepository.class);
    bonsaiService = new BonsaiService(applicationContext, bonsaiRepository);
  }

  @Test
  void shouldReturnNullWhenSaveBonsaiIsNull() {
    //the repo save is not throwing IllegalArgumentException, not sure why
    Bonsai x = bonsaiService.save(null);
    assertEquals(x, null);
  }

  @Test
  void shouldSaveBonsai() {
    Bonsai bonsai = new Bonsai();
    bonsai.setId(1);

    bonsaiService.save(bonsai);

    verify(bonsaiRepository).save(bonsai);
    verifyNoMoreInteractions(bonsaiRepository);
  }

  @Test
  void shouldFindAllBonsais() {
    bonsaiService.findAll();

    verify(bonsaiRepository).findAll();
    verifyNoMoreInteractions(bonsaiRepository);
  }

  @Test
  void shouldFindAllBonsaisPageable() {
    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);

    bonsaiService.findAll(paging);

    verify(bonsaiRepository).findAll(paging);
    verifyNoMoreInteractions(bonsaiRepository);
  }

  @Test
  void shouldDeleteBonsai() {
    Bonsai bonsai = new Bonsai();
    bonsai.setId(1);

    bonsaiService.delete(bonsai);

    verify(bonsaiRepository).delete(bonsai);
    verifyNoMoreInteractions(bonsaiRepository);
  }

  @Test
  void shouldFindByIdBonsai() {
    bonsaiService.findById(1);

    verify(bonsaiRepository).findById(1);
    verifyNoMoreInteractions(bonsaiRepository);
  }

  @Test
  void shouldCountBonsai() {
    bonsaiService.count();

    verify(bonsaiRepository).count();
    verifyNoMoreInteractions(bonsaiRepository);
  }

  @Test
  void shouldFindByNameContaining() {
    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);

    bonsaiService.findByNameContaining("test", paging);

    verify(bonsaiRepository).findByNameContaining("test", paging);
    verifyNoMoreInteractions(bonsaiRepository);
  }


}
