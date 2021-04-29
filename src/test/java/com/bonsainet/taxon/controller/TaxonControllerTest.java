package com.bonsainet.taxon.controller;

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

import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.controller.TaxonController;
import com.bonsainet.bonsai.service.TaxonService;
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

@ContextConfiguration(classes = Taxon.class)
@WebMvcTest(controllers = TaxonController.class)
public class TaxonControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private TaxonService taxonService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @BeforeEach
  void setup() {
    TaxonController taxonController = new TaxonController(taxonService);
    mockMvc = MockMvcBuilders.standaloneSetup(taxonController)
        .setMessageConverters(jacksonMessageConverter)
        .build();
  }

  @Test
  void findTaxaTest() throws Exception {
    int taxonId = 1;
    Taxon taxon = new Taxon();
    taxon.setId(taxonId);
    ArrayList<Taxon> taxaList = new ArrayList<>();
    taxaList.add(taxon);

    when(taxonService.findAll()).thenReturn(taxaList);

    this.mockMvc.perform(get("/taxon/taxa"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(taxaList.size())))
        .andExpect(jsonPath("$[0].id", is(taxonId)));

    verify(taxonService).findAll();
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void taxaCountTest() throws Exception {
    Long count = 1L;
    when(taxonService.count()).thenReturn(count);

    this.mockMvc.perform(get("/taxon/taxa/count"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(count.toString()));

    verify(taxonService).count();
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void findTaxaForPagePageAndSizeTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    int taxonId = 1;

    Taxon taxon = new Taxon();
    taxon.setId(taxonId);
    ArrayList<Taxon> taxaList = new ArrayList<>();
    taxaList.add(taxon);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "fullName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<Taxon> pageTaxon = new PageImpl<>(taxaList, pageRequest, taxaList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(taxonService.findAll(paging)).thenReturn(pageTaxon);

    this.mockMvc.perform(get("/taxon/taxa_page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(taxonId)));

    verify(taxonService).findAll(paging);
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void findTaxaForPagePageAndSizeOutOfRangeTest() throws Exception {
    int passedSize = -1;
    int passedPage = -1;
    int fixedSize = 1;
    int fixedPage = 0;
    int taxonId = 1;

    Taxon taxon = new Taxon();
    taxon.setId(taxonId);
    ArrayList<Taxon> taxaList = new ArrayList<>();
    taxaList.add(taxon);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "fullName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(fixedPage, fixedSize, sortFinal);
    Page<Taxon> pageTaxon = new PageImpl<>(taxaList, pageRequest, taxaList.size());

    Pageable paging = PageRequest.of(fixedPage, fixedSize, sortFinal);
    when(taxonService.findAll(paging)).thenReturn(pageTaxon);

    this.mockMvc.perform(get("/taxon/taxa_page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(taxonId)));

    verify(taxonService).findAll(paging);
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void findTaxonsForPagePageAndSizeOutOfRangeTest2() throws Exception {
    int passedSize = 1000;
    int passedPage = 0;
    int fixedSize = 100;
    int taxonId = 1;

    ArrayList<Taxon> taxaList = new ArrayList<>();
    for (int i = 0; i < fixedSize; i++) {
      Taxon taxon = new Taxon();
      taxon.setId(taxonId);
      taxaList.add(taxon);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "fullName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, fixedSize, sortFinal);
    Page<Taxon> pageTaxon = new PageImpl<>(taxaList, pageRequest, taxaList.size());

    Pageable paging = PageRequest.of(passedPage, fixedSize, sortFinal);
    when(taxonService.findAll(paging)).thenReturn(pageTaxon);

    this.mockMvc.perform(get("/taxon/taxa_page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(taxonId)));

    verify(taxonService).findAll(paging);
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void findTaxaForPageSortAndDirTest() throws Exception {
    int passedPage = 0;

    ArrayList<Taxon> taxaList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      Taxon taxon = new Taxon();
      taxon.setId(i);
      taxon.setCountBonsais(1-i);
      taxaList.add(taxon);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "fullName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, taxaList.size(), sortFinal);
    Page<Taxon> pageTaxon = new PageImpl<>(taxaList, pageRequest, taxaList.size());

    Pageable paging = PageRequest.of(passedPage, taxaList.size(), sortFinal);
    when(taxonService.findAll(paging)).thenReturn(pageTaxon);

    this.mockMvc.perform(get("/taxon/taxa_page?page=" + passedPage + "&size=" + taxaList.size() + "&sort=id&dir=asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(taxaList.size())))
        .andExpect(jsonPath("$.content[0].id", is(0)))
        .andExpect(jsonPath("$.content[0].countBonsais", is(1)));

    verify(taxonService).findAll(paging);
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void findTaxaForPageSortAndDirTest2() throws Exception {
    int passedPage = 0;

    ArrayList<Taxon> taxaList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      Taxon taxon = new Taxon();
      taxon.setId(1-i);
      taxon.setCountBonsais(i);
      taxaList.add(taxon);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.DESC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "fullName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, taxaList.size(), sortFinal);
    Page<Taxon> pageTaxon = new PageImpl<>(taxaList, pageRequest, taxaList.size());

    Pageable paging = PageRequest.of(passedPage, taxaList.size(), sortFinal);
    when(taxonService.findAll(paging)).thenReturn(pageTaxon);

    this.mockMvc.perform(get("/taxon/taxa_page?page=" + passedPage + "&size=" + taxaList.size() + "&sort=id&dir=desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(taxaList.size())))
        .andExpect(jsonPath("$.content[0].id", is(1)))
        .andExpect(jsonPath("$.content[0].countBonsais", is(0)));

    verify(taxonService).findAll(paging);
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void findTaxaForPageFilterTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    String passedFilter = "test";

    int taxonId = 1;

    Taxon taxon = new Taxon();
    taxon.setId(taxonId);
    ArrayList<Taxon> taxaList = new ArrayList<>();
    taxaList.add(taxon);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "fullName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<Taxon> pageTaxon = new PageImpl<>(taxaList, pageRequest, taxaList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(taxonService.findByFullNameContaining(passedFilter, paging)).thenReturn(pageTaxon);

    this.mockMvc.perform(get("/taxon/taxa_page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(taxonId)));

    verify(taxonService).findByFullNameContaining(passedFilter, paging);
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void saveTaxonTest() throws Exception {
    int taxonId = 1;

    Taxon taxon = new Taxon();
    taxon.setId(taxonId);

    when(taxonService.save(taxon)).thenReturn(taxon);

    this.mockMvc.perform(put("/taxon/taxon")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(taxon)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", is(taxonId)));

    verify(taxonService).save(taxon);
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void deleteTaxonTest() throws Exception {
    int taxonId = 1;

    Taxon taxon = new Taxon();
    taxon.setId(taxonId);

    when(taxonService.findById(taxonId)).thenReturn(Optional.of(taxon));

    this.mockMvc.perform(delete("/taxon/taxon/del/1"))
        .andExpect(status().isOk());

    verify(taxonService).findById(taxonId);
    verify(taxonService).delete(taxon);
    verifyNoMoreInteractions(taxonService);
  }

  @Test
  void deleteTaxonNotFoundTest() throws Exception {
    int taxonId = 1;

    Taxon taxon = new Taxon();
    taxon.setId(taxonId);

    when(taxonService.findById(taxonId)).thenReturn(Optional.empty());

    this.mockMvc.perform(delete("/taxon/taxon/del/1"))
        .andExpect(status().isNotFound());

    verify(taxonService).findById(taxonId);
    verifyNoMoreInteractions(taxonService);
  }
  
}
