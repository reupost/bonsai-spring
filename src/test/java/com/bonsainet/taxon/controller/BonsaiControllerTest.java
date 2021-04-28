package com.bonsainet.taxon.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonsainet.bonsai.controller.BonsaiController;
import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.service.BonsaiService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

@ContextConfiguration(classes = Bonsai.class)
@WebMvcTest(controllers = BonsaiController.class)
public class BonsaiControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private BonsaiService bonsaiService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @BeforeEach
  void setup() {
    BonsaiController bonsaiController = new BonsaiController(bonsaiService);
    mockMvc = MockMvcBuilders.standaloneSetup(bonsaiController)
        .setMessageConverters(jacksonMessageConverter)
        .build();
  }

  @Test
  void findBonsaisTest() throws Exception {
    Bonsai bonsai = new Bonsai();
    bonsai.setId(1);
    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    bonsaiList.add(bonsai);

    when(bonsaiService.findAll()).thenReturn(bonsaiList);

    this.mockMvc.perform(get("/bonsai/bonsais"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)));

    verify(bonsaiService).findAll();
    verifyNoMoreInteractions(bonsaiService);
  }

  @Test
  void bonsaisCountTest() throws Exception {
    Long count = 1L;
    when(bonsaiService.count()).thenReturn(count);

    this.mockMvc.perform(get("/bonsai/bonsais/count"))
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

    Bonsai bonsai = new Bonsai();
    bonsai.setId(1);
    ArrayList<Bonsai> bonsaiList = new ArrayList<>();
    bonsaiList.add(bonsai);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<Bonsai> pageBonsai = new PageImpl<>(bonsaiList, pageRequest, bonsaiList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(bonsaiService.findAll(paging)).thenReturn(pageBonsai);

    this.mockMvc.perform(get("/bonsai/bonsais_page?page=0&size=1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].id", is(1)));

    verify(bonsaiService).findAll(paging);
    verifyNoMoreInteractions(bonsaiService);
  }

}
