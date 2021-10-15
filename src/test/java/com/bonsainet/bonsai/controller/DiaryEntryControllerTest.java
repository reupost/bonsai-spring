package com.bonsainet.bonsai.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.DiaryEntry;
import com.bonsainet.bonsai.model.DiaryEntryDTO;
import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.service.DiaryEntryService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = DiaryEntry.class)
@WebMvcTest(controllers = DiaryEntryController.class)
public class DiaryEntryControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private DiaryEntryService diaryEntryService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @BeforeEach
  void setup() {
    DiaryEntryController diaryEntryController = new DiaryEntryController(diaryEntryService);
    mockMvc = MockMvcBuilders.standaloneSetup(diaryEntryController)
        .setMessageConverters(jacksonMessageConverter)
        .build();
  }

  @Test
  void findDiaryEntriesTest() throws Exception {
    int diaryEntryId = 1;
    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(diaryEntryId);
    ArrayList<DiaryEntry> diaryEntryList = new ArrayList<>();
    diaryEntryList.add(diaryEntry);

    when(diaryEntryService.findAll()).thenReturn(diaryEntryList);

    this.mockMvc.perform(get("/diaryEntry/diaryEntries"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(diaryEntryList.size())))
        .andExpect(jsonPath("$[0].id", is(diaryEntryId)));

    verify(diaryEntryService).findAll();
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void diaryEntryCountTest() throws Exception {
    Long count = 1L;
    when(diaryEntryService.count()).thenReturn(count);

    this.mockMvc.perform(get("/diaryEntry/count"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(count.toString()));

    verify(diaryEntryService).count();
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void findDiaryEntryForPagePageAndSizeTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    int diaryEntryId = 1;

    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(diaryEntryId);
    ArrayList<DiaryEntry> diaryEntryList = new ArrayList<>();
    diaryEntryList.add(diaryEntry);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "entryDate"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<DiaryEntry> pageDiaryEntry = new PageImpl<>(diaryEntryList, pageRequest, diaryEntryList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(diaryEntryService.findAll(paging)).thenReturn(pageDiaryEntry);

    this.mockMvc.perform(get("/diaryEntry/page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(diaryEntryId)));

    verify(diaryEntryService).findAll(paging);
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void findDiaryEntriesForPagePageAndSizeOutOfRangeTest() throws Exception {
    int passedSize = -1;
    int passedPage = -1;
    int fixedSize = 1;
    int fixedPage = 0;
    int diaryEntryId = 1;

    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(diaryEntryId);
    ArrayList<DiaryEntry> diaryEntryList = new ArrayList<>();
    diaryEntryList.add(diaryEntry);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "entryDate"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(fixedPage, fixedSize, sortFinal);
    Page<DiaryEntry> pageDiaryEntry = new PageImpl<>(diaryEntryList, pageRequest, diaryEntryList.size());

    Pageable paging = PageRequest.of(fixedPage, fixedSize, sortFinal);
    when(diaryEntryService.findAll(paging)).thenReturn(pageDiaryEntry);

    this.mockMvc.perform(get("/diaryEntry/page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(diaryEntryId)));

    verify(diaryEntryService).findAll(paging);
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void findDiaryEntriesForPagePageAndSizeOutOfRangeTest2() throws Exception {
    int passedSize = 1000;
    int passedPage = 0;
    int fixedSize = GeneralControllerHelper.MAX_LIST_SIZE;
    int diaryEntryId = 1;

    ArrayList<DiaryEntry> diaryEntryList = new ArrayList<>();
    for (int i = 0; i < fixedSize; i++) {
      DiaryEntry diaryEntry = new DiaryEntry();
      diaryEntry.setId(diaryEntryId);
      diaryEntryList.add(diaryEntry);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "entryDate"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, fixedSize, sortFinal);
    Page<DiaryEntry> pageDiaryEntry = new PageImpl<>(diaryEntryList, pageRequest, diaryEntryList.size());

    Pageable paging = PageRequest.of(passedPage, fixedSize, sortFinal);
    when(diaryEntryService.findAll(paging)).thenReturn(pageDiaryEntry);

    this.mockMvc.perform(get("/diaryEntry/page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(diaryEntryId)));

    verify(diaryEntryService).findAll(paging);
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void findDiaryEntriesForPageSortAndDirTest() throws Exception {
    int passedPage = 0;
    LocalDateTime dte = LocalDateTime.of(2021, 12, 1, 1, 1, 1);

    ArrayList<DiaryEntry> diaryEntryList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      DiaryEntry diaryEntry = new DiaryEntry();
      diaryEntry.setId(i);
      diaryEntry.setEntryDate(dte.minus(i, ChronoUnit.MONTHS));
      diaryEntryList.add(diaryEntry);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "entryDate"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, diaryEntryList.size(), sortFinal);
    Page<DiaryEntry> pageDiaryEntry = new PageImpl<>(diaryEntryList, pageRequest, diaryEntryList.size());

    Pageable paging = PageRequest.of(passedPage, diaryEntryList.size(), sortFinal);
    when(diaryEntryService.findAll(paging)).thenReturn(pageDiaryEntry);

    this.mockMvc.perform(get("/diaryEntry/page?page=" + passedPage + "&size=" + diaryEntryList.size() + "&sort=id&dir=asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(diaryEntryList.size())))
        .andExpect(jsonPath("$.content[0].id", is(0)))
        .andExpect(jsonPath("$.content[0].entryDate", is(dte.toString())));

    verify(diaryEntryService).findAll(paging);
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void findDiaryEntriesForPageSortAndDirTest2() throws Exception {
    int passedPage = 0;
    LocalDateTime dte = LocalDateTime.of(2021, 12, 1, 1, 1, 1);

    ArrayList<DiaryEntry> diaryEntryList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      DiaryEntry diaryEntry = new DiaryEntry();
      diaryEntry.setId(1-i);
      diaryEntry.setEntryDate(dte.minus(i, ChronoUnit.MONTHS));
      diaryEntryList.add(diaryEntry);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.DESC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "entryDate"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, diaryEntryList.size(), sortFinal);
    Page<DiaryEntry> pageDiaryEntry = new PageImpl<>(diaryEntryList, pageRequest, diaryEntryList.size());

    Pageable paging = PageRequest.of(passedPage, diaryEntryList.size(), sortFinal);
    when(diaryEntryService.findAll(paging)).thenReturn(pageDiaryEntry);

    this.mockMvc.perform(get("/diaryEntry/page?page=" + passedPage + "&size=" + diaryEntryList.size() + "&sort=id&dir=desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(diaryEntryList.size())))
        .andExpect(jsonPath("$.content[0].id", is(1)))
        .andExpect(jsonPath("$.content[0].entryDate", is(dte.toString())));

    verify(diaryEntryService).findAll(paging);
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void findDiaryEntriesForPageFilterTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    String passedFilter = "test";

    int diaryEntryId = 1;

    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(diaryEntryId);
    ArrayList<DiaryEntry> diaryEntryList = new ArrayList<>();
    diaryEntryList.add(diaryEntry);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "entryDate"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<DiaryEntry> pageDiaryEntry = new PageImpl<>(diaryEntryList, pageRequest, diaryEntryList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(diaryEntryService.findByEntryTextOrBonsaiContaining(passedFilter, paging)).thenReturn(pageDiaryEntry);

    this.mockMvc.perform(get("/diaryEntry/page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(diaryEntryId)));

    verify(diaryEntryService).findByEntryTextOrBonsaiContaining(passedFilter, paging);
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void saveDiaryEntryTest() throws Exception {
    int diaryEntryId = 1;

    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(diaryEntryId);
    diaryEntry.setEntryDate(LocalDateTime.now());

    when(diaryEntryService.save(diaryEntry)).thenReturn(diaryEntry);

    this.mockMvc.perform(put("/diaryEntry")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diaryEntry)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", is(diaryEntryId)));

    verify(diaryEntryService).save(diaryEntry);
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void updateDiaryEntryDtoTest() throws Exception {
    int diaryEntryId = 1;

    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(diaryEntryId);
    DiaryEntryDTO diaryEntryDto = new DiaryEntryDTO();
    diaryEntryDto.setId(diaryEntryId);

    when(diaryEntryService.save(diaryEntry)).thenReturn(diaryEntry);
    when(diaryEntryService.toDiaryEntry(diaryEntryDto)).thenReturn(diaryEntry);
    when(diaryEntryService.toDto(diaryEntry)).thenReturn(diaryEntryDto);

    this.mockMvc.perform(put("/diaryEntry/dto")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diaryEntryDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", is(diaryEntryId)));

    verify(diaryEntryService).save(diaryEntry);
  }

  @Test
  void deleteDiaryEntryTest() throws Exception {
    int diaryEntryId = 1;

    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(diaryEntryId);

    when(diaryEntryService.findById(diaryEntryId)).thenReturn(Optional.of(diaryEntry));

    this.mockMvc.perform(delete("/diaryEntry/1"))
        .andExpect(status().isOk());

    verify(diaryEntryService).findById(diaryEntryId);
    verify(diaryEntryService).delete(diaryEntry);
    verifyNoMoreInteractions(diaryEntryService);
  }

  @Test
  void deleteDiaryEntryNotFoundTest() throws Exception {
    int diaryEntryId = 1;

    DiaryEntry diaryEntry = new DiaryEntry();
    diaryEntry.setId(diaryEntryId);

    when(diaryEntryService.findById(diaryEntryId)).thenReturn(Optional.empty());

    this.mockMvc.perform(delete("/diaryEntry/1"))
        .andExpect(status().isNotFound());

    verify(diaryEntryService).findById(diaryEntryId);
    verifyNoMoreInteractions(diaryEntryService);
  }

}
