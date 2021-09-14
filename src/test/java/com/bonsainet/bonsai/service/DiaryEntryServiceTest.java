package com.bonsainet.bonsai.service;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.DiaryEntry;
import com.bonsainet.bonsai.repository.DiaryEntryRepository;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

class DiaryEntryServiceTest {

  private DiaryEntryService diaryEntryService;
  private ApplicationContext applicationContext;

  DiaryEntryRepository diaryEntryRepository;

  @BeforeEach
  void setup() {
    applicationContext = mock(ApplicationContext.class);
    diaryEntryRepository = mock(DiaryEntryRepository.class);
    diaryEntryService = new DiaryEntryService(applicationContext, diaryEntryRepository);
  }

  @Test
  void shouldReturnNullWhenSaveDiaryEntryIsNull() {
    //the repo save is not throwing IllegalArgumentException, not sure why
    DiaryEntry x = diaryEntryService.save(null);
    assertNull(x);
  }

  @Test
  void shouldSaveDiaryEntry() {
    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(1);

    diaryEntryService.save(diaryEntry);

    verify(diaryEntryRepository).save(diaryEntry);
    verifyNoMoreInteractions(diaryEntryRepository);
  }

  @Test
  void shouldFindAllDiaryEntries() {
    diaryEntryService.findAll();

    verify(diaryEntryRepository).findAll();
    verifyNoMoreInteractions(diaryEntryRepository);
  }

  @Test
  void shouldFindAllDiaryEntriesPageable() {
    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "entryDate"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);

    diaryEntryService.findAll(paging);

    verify(diaryEntryRepository).findAll(paging);
    verifyNoMoreInteractions(diaryEntryRepository);
  }

  @Test
  void shouldDeleteDiaryEntry() {
    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(1);

    diaryEntryService.delete(diaryEntry);

    verify(diaryEntryRepository).delete(diaryEntry);
    verifyNoMoreInteractions(diaryEntryRepository);
  }

  @Test
  void shouldFindByIdDiaryEntry() {
    diaryEntryService.findById(1);

    verify(diaryEntryRepository).findById(1);
    verifyNoMoreInteractions(diaryEntryRepository);
  }

  @Test
  void shouldCountDiaryEntry() {
    diaryEntryService.count();

    verify(diaryEntryRepository).count();
    verifyNoMoreInteractions(diaryEntryRepository);
  }

  @Test
  void shouldFindByBonsai() {
    Bonsai b = new Bonsai();
    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "entryDate"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);

    diaryEntryService.findByBonsai(b, paging);

    verify(diaryEntryRepository).findByBonsai(b, paging);
    verifyNoMoreInteractions(diaryEntryRepository);
  }

  @Test
  void shouldFindByEntryTextContaining() {
    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "entryDate"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);

    diaryEntryService.findByEntryTextContaining("test", paging);

    verify(diaryEntryRepository).findByEntryTextContaining("test", paging);
    verifyNoMoreInteractions(diaryEntryRepository);
  }
}
