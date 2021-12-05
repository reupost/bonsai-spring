package com.bonsainet.bonsai.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
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
import com.bonsainet.bonsai.service.BonsaiService;
import com.bonsainet.bonsai.service.TaxonService;
import com.bonsainet.bonsai.service.UserService;
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
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(classes = Bonsai.class)
@WebMvcTest(controllers = BonsaiController.class)
public class BonsaiControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private BonsaiService bonsaiService;
  @MockBean
  private UserService userService;
  @MockBean
  private TaxonService taxonService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @BeforeEach
  void setup() {
    BonsaiController bonsaiController = new BonsaiController(bonsaiService, userService, taxonService);
    mockMvc = MockMvcBuilders.standaloneSetup(bonsaiController)
        .setMessageConverters(jacksonMessageConverter)
        .build();
  }

  @Test
  void getBonsaiByIdTest() throws Exception {
    int bonsaiId = 1;
    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);

    when(bonsaiService.findById(bonsaiId)).thenReturn(Optional.of(bonsai));

    this.mockMvc.perform(get("/bonsai/" + bonsaiId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", is(bonsaiId)));

    verify(bonsaiService).findById(bonsaiId);
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void findBonsaisTest() throws Exception {
    int bonsaiId = 1;
    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);
    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    bonsaiList.add(bonsai);

    when(bonsaiService.findAll()).thenReturn(bonsaiList);

    this.mockMvc.perform(get("/bonsai/bonsais"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(bonsaiList.size())))
        .andExpect(jsonPath("$[0].id", is(bonsaiId)));

    verify(bonsaiService).findAll();
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void bonsaisCountTest() throws Exception {
    Long count = 1L;
    when(bonsaiService.count()).thenReturn(count);

    this.mockMvc.perform(get("/bonsai/count"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(count.toString()));

    verify(bonsaiService).count();
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void findBonsaisForPagePageAndSizeTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    int bonsaiId = 1;

    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);
    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    bonsaiList.add(bonsai);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<Bonsai> pageBonsai = new PageImpl<>(bonsaiList, pageRequest, bonsaiList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(bonsaiService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(bonsaiId)));

    verify(bonsaiService).findAll(paging);
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void findBonsaisForPagePageAndSizeOutOfRangeTest() throws Exception {
    int passedSize = -1;
    int passedPage = -1;
    int fixedSize = 1;
    int fixedPage = 0;
    int bonsaiId = 1;

    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);
    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    bonsaiList.add(bonsai);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(fixedPage, fixedSize, sortFinal);
    Page<Bonsai> pageBonsai = new PageImpl<>(bonsaiList, pageRequest, bonsaiList.size());

    Pageable paging = PageRequest.of(fixedPage, fixedSize, sortFinal);
    when(bonsaiService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(bonsaiId)));

    verify(bonsaiService).findAll(paging);
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void findBonsaisForPagePageAndSizeOutOfRangeTest2() throws Exception {
    int passedSize = 1000;
    int passedPage = 0;
    int fixedSize = GeneralControllerHelper.MAX_LIST_SIZE;
    int bonsaiId = 1;

    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    for (int i = 0; i < fixedSize; i++) {
      Bonsai bonsai = new Bonsai();
      bonsai.setId(bonsaiId);
      bonsaiList.add(bonsai);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, fixedSize, sortFinal);
    Page<Bonsai> pageBonsai = new PageImpl<>(bonsaiList, pageRequest, bonsaiList.size());

    Pageable paging = PageRequest.of(passedPage, fixedSize, sortFinal);
    when(bonsaiService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(bonsaiId)));

    verify(bonsaiService).findAll(paging);
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void findBonsaisForPageSortAndDirTest() throws Exception {
    int passedPage = 0;

    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      Bonsai bonsai = new Bonsai();
      bonsai.setId(i);
      bonsai.setTag(1-i);
      bonsaiList.add(bonsai);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, bonsaiList.size(), sortFinal);
    Page<Bonsai> pageBonsai = new PageImpl<>(bonsaiList, pageRequest, bonsaiList.size());

    Pageable paging = PageRequest.of(passedPage, bonsaiList.size(), sortFinal);
    when(bonsaiService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/page?page=" + passedPage + "&size=" + bonsaiList.size() + "&sort=id&dir=asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(bonsaiList.size())))
        .andExpect(jsonPath("$.content[0].id", is(0)))
        .andExpect(jsonPath("$.content[0].tag", is(1)));

    verify(bonsaiService).findAll(paging);
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void findBonsaisForPageSortAndDirTest2() throws Exception {
    int passedPage = 0;

    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      Bonsai bonsai = new Bonsai();
      bonsai.setId(1-i);
      bonsai.setTag(i);
      bonsaiList.add(bonsai);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.DESC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, bonsaiList.size(), sortFinal);
    Page<Bonsai> pageBonsai = new PageImpl<>(bonsaiList, pageRequest, bonsaiList.size());

    Pageable paging = PageRequest.of(passedPage, bonsaiList.size(), sortFinal);
    when(bonsaiService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/page?page=" + passedPage + "&size=" + bonsaiList.size() + "&sort=id&dir=desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(bonsaiList.size())))
        .andExpect(jsonPath("$.content[0].id", is(1)))
        .andExpect(jsonPath("$.content[0].tag", is(0)));

    verify(bonsaiService).findAll(paging);
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void findBonsaisForPageFilterTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    String passedFilter = "test";

    int bonsaiId = 1;

    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);
    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    bonsaiList.add(bonsai);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<Bonsai> pageBonsai = new PageImpl<>(bonsaiList, pageRequest, bonsaiList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(bonsaiService.findByNameOrTaxonContaining(passedFilter, paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(bonsaiId)));

    verify(bonsaiService).findByNameOrTaxonContaining(passedFilter, paging);
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void saveBonsaiTest() throws Exception {
    int bonsaiId = 1;

    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);

    when(bonsaiService.save(bonsai)).thenReturn(bonsai);

    this.mockMvc.perform(put("/bonsai")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(bonsai)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", is(bonsaiId)));

    verify(bonsaiService).save(bonsai);
    verifyNoMoreInteractions(bonsaiService);
    //TODO: take a look at new RestTemplate().exchange(url, HttpMethod.POST, ...)
  }

  @Test
  void deleteBonsaiTest() throws Exception {
    int bonsaiId = 1;

    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);

    when(bonsaiService.findById(bonsaiId)).thenReturn(Optional.of(bonsai));

    this.mockMvc.perform(delete("/bonsai/1"))
        .andExpect(status().isOk());

    verify(bonsaiService).findById(bonsaiId);
    verify(bonsaiService).delete(bonsai);
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void deleteBonsaiNotFoundTest() throws Exception {
    int bonsaiId = 1;

    Bonsai bonsai = new Bonsai();
    bonsai.setId(bonsaiId);

    when(bonsaiService.findById(bonsaiId)).thenReturn(Optional.empty());

    this.mockMvc.perform(delete("/bonsai/1"))
        .andExpect(status().isNotFound());

    verify(bonsaiService).findById(bonsaiId);
    verifyNoMoreInteractions(bonsaiService);
  }

}
