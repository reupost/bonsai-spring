package com.bonsainet.taxon.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.repository.TaxonRepository;
import com.bonsainet.bonsai.service.TaxonService;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

class TaxonServiceTest {

  private TaxonService taxonService;
  private ApplicationContext applicationContext;

  TaxonRepository taxonRepository;

  @BeforeEach
  void setup() {
    applicationContext = mock(ApplicationContext.class);
    taxonRepository = mock(TaxonRepository.class);
    taxonService = new TaxonService(applicationContext, taxonRepository);
  }

  @Test
  void shouldThrowExceptionWhenSaveTaxonIsNull() {
    assertThrows(IllegalArgumentException.class, () -> taxonService.save(null));
  }

  @Test
  void shouldSaveTaxon() {
    Taxon taxon = new Taxon();
    taxon.setId(1);

    taxonService.save(taxon);

    verify(taxonRepository).save(taxon);
    verifyNoMoreInteractions(taxonRepository);
  }

  @Test
  void shouldFindAllTaxa() {
    taxonService.findAll();

    verify(taxonRepository).findAll();
    verifyNoMoreInteractions(taxonRepository);
  }

  @Test
  void shouldFindAllTaxaPageable() {
    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "family"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);

    taxonService.findAll(paging);

    verify(taxonRepository).findAll(paging);
    verifyNoMoreInteractions(taxonRepository);
  }

  @Test
  void shouldDeleteTaxon() {
    Taxon taxon = new Taxon();
    taxon.setId(1);

    taxonService.delete(taxon);

    verify(taxonRepository).delete(taxon);
    verifyNoMoreInteractions(taxonRepository);
  }

  @Test
  void shouldFindByIdTaxon() {
    taxonService.findById(1);

    verify(taxonRepository).findById(1);
    verifyNoMoreInteractions(taxonRepository);
  }

  @Test
  void shouldCountTaxon() {
    taxonService.count();

    verify(taxonRepository).count();
    verifyNoMoreInteractions(taxonRepository);
  }

  @Test
  void shouldFindByFullnameContaining() {
    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "family"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);

    taxonService.findByFullNameContaining("test", paging);

    verify(taxonRepository).findByFullNameContaining("test", paging);
    verifyNoMoreInteractions(taxonRepository);
  }
}
