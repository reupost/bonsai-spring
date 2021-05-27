package com.bonsainet.bonsai.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.service.BonsaiDTOService;
import java.util.ArrayList;
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

@ContextConfiguration(classes = BonsaiDTO.class)
@WebMvcTest(controllers = BonsaiDTOController.class)
public class BonsaiDTOControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private BonsaiDTOService bonsaiDtoService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @BeforeEach
  void setup() {
    BonsaiDTOController bonsaiDtoController = new BonsaiDTOController(bonsaiDtoService);
    mockMvc = MockMvcBuilders.standaloneSetup(bonsaiDtoController)
        .setMessageConverters(jacksonMessageConverter)
        .build();
  }

  @Test
  void findBonsaiDTOsTest() throws Exception {
    int bonsaiId = 1;
    BonsaiDTO bonsaiDto = new BonsaiDTO();
    bonsaiDto.setId(bonsaiId);
    ArrayList<BonsaiDTO> bonsaiDtoList = new ArrayList<>();
    bonsaiDtoList.add(bonsaiDto);

    when(bonsaiDtoService.findAll()).thenReturn(bonsaiDtoList);

    this.mockMvc.perform(get("/bonsai/bonsaisdto"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(bonsaiDtoList.size())))
        .andExpect(jsonPath("$[0].id", is(bonsaiId)));

    verify(bonsaiDtoService).findAll();
    verifyNoMoreInteractions(bonsaiDtoService);
  }

  @Test
  void bonsaiDTOsCountTest() throws Exception {
    Long count = 1L;
    when(bonsaiDtoService.count()).thenReturn(count);

    this.mockMvc.perform(get("/bonsai/bonsaisdto/count"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(count.toString()));

    verify(bonsaiDtoService).count();
    verifyNoMoreInteractions(bonsaiDtoService);
  }

  @Test
  void findBonsaiDTOsForPagePageAndSizeTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    int bonsaiId = 1;

    BonsaiDTO bonsaiDto = new BonsaiDTO();
    bonsaiDto.setId(bonsaiId);
    ArrayList<BonsaiDTO> bonsaiDtoList = new ArrayList<>();
    bonsaiDtoList.add(bonsaiDto);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<BonsaiDTO> pageBonsai = new PageImpl<>(bonsaiDtoList, pageRequest, bonsaiDtoList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(bonsaiDtoService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/bonsaisdto_page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(bonsaiId)));

    verify(bonsaiDtoService).findAll(paging);
    verifyNoMoreInteractions(bonsaiDtoService);
  }

  @Test
  void findBonsaiDTOsForPagePageAndSizeOutOfRangeTest() throws Exception {
    int passedSize = -1;
    int passedPage = -1;
    int fixedSize = 1;
    int fixedPage = 0;
    int bonsaiId = 1;

    BonsaiDTO bonsaiDto = new BonsaiDTO();
    bonsaiDto.setId(bonsaiId);
    ArrayList<BonsaiDTO> bonsaiDtoList = new ArrayList<>();
    bonsaiDtoList.add(bonsaiDto);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(fixedPage, fixedSize, sortFinal);
    Page<BonsaiDTO> pageBonsai = new PageImpl<>(bonsaiDtoList, pageRequest, bonsaiDtoList.size());

    Pageable paging = PageRequest.of(fixedPage, fixedSize, sortFinal);
    when(bonsaiDtoService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/bonsaisdto_page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(bonsaiId)));

    verify(bonsaiDtoService).findAll(paging);
    verifyNoMoreInteractions(bonsaiDtoService);
  }

  @Test
  void findBonsaiDTOsForPagePageAndSizeOutOfRangeTest2() throws Exception {
    int passedSize = 1000;
    int passedPage = 0;
    int fixedSize = 100;
    int bonsaiId = 1;

    ArrayList<BonsaiDTO> bonsaiDtoList = new ArrayList<>();
    for (int i = 0; i < fixedSize; i++) {
      BonsaiDTO bonsaiDto = new BonsaiDTO();
      bonsaiDto.setId(bonsaiId);
      bonsaiDtoList.add(bonsaiDto);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, fixedSize, sortFinal);
    Page<BonsaiDTO> pageBonsai = new PageImpl<>(bonsaiDtoList, pageRequest, bonsaiDtoList.size());

    Pageable paging = PageRequest.of(passedPage, fixedSize, sortFinal);
    when(bonsaiDtoService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/bonsaisdto_page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(bonsaiId)));

    verify(bonsaiDtoService).findAll(paging);
    verifyNoMoreInteractions(bonsaiDtoService);
  }

  @Test
  void findBonsaiDTOsForPageSortAndDirTest() throws Exception {
    int passedPage = 0;

    ArrayList<BonsaiDTO> bonsaiDtoList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      BonsaiDTO bonsaiDto = new BonsaiDTO();
      bonsaiDto.setId(i);
      bonsaiDto.setTag(1-i);
      bonsaiDtoList.add(bonsaiDto);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, bonsaiDtoList.size(), sortFinal);
    Page<BonsaiDTO> pageBonsai = new PageImpl<>(bonsaiDtoList, pageRequest, bonsaiDtoList.size());

    Pageable paging = PageRequest.of(passedPage, bonsaiDtoList.size(), sortFinal);
    when(bonsaiDtoService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/bonsaisdto_page?page=" + passedPage + "&size=" + bonsaiDtoList.size() + "&sort=id&dir=asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(bonsaiDtoList.size())))
        .andExpect(jsonPath("$.content[0].id", is(0)))
        .andExpect(jsonPath("$.content[0].tag", is(1)));

    verify(bonsaiDtoService).findAll(paging);
    verifyNoMoreInteractions(bonsaiDtoService);
  }

  @Test
  void findBonsaiDTOsForPageSortAndDirTest2() throws Exception {
    int passedPage = 0;

    ArrayList<BonsaiDTO> bonsaiDtoList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      BonsaiDTO bonsaiDto = new BonsaiDTO();
      bonsaiDto.setId(1-i);
      bonsaiDto.setTag(i);
      bonsaiDtoList.add(bonsaiDto);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.DESC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, bonsaiDtoList.size(), sortFinal);
    Page<BonsaiDTO> pageBonsai = new PageImpl<>(bonsaiDtoList, pageRequest, bonsaiDtoList.size());

    Pageable paging = PageRequest.of(passedPage, bonsaiDtoList.size(), sortFinal);
    when(bonsaiDtoService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/bonsaisdto_page?page=" + passedPage + "&size=" + bonsaiDtoList.size() + "&sort=id&dir=desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(bonsaiDtoList.size())))
        .andExpect(jsonPath("$.content[0].id", is(1)))
        .andExpect(jsonPath("$.content[0].tag", is(0)));

    verify(bonsaiDtoService).findAll(paging);
    verifyNoMoreInteractions(bonsaiDtoService);
  }

  @Test
  void findBonsaiDTOsForPageFilterTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    String passedFilter = "test";

    int bonsaiId = 1;

    BonsaiDTO bonsaiDto = new BonsaiDTO();
    bonsaiDto.setId(bonsaiId);
    ArrayList<BonsaiDTO> bonsaiDtoList = new ArrayList<>();
    bonsaiDtoList.add(bonsaiDto);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<BonsaiDTO> pageBonsai = new PageImpl<>(bonsaiDtoList, pageRequest, bonsaiDtoList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(bonsaiDtoService.findByNameContaining(passedFilter, paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/bonsaisdto_page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(bonsaiId)));

    verify(bonsaiDtoService).findByNameContaining(passedFilter, paging);
    verifyNoMoreInteractions(bonsaiDtoService);
  }

}
