package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.service.PicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = Pic.class)
@WebMvcTest(controllers = PicController.class)
public class PicControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PicService picService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @BeforeEach
    void setup() {
        PicController picController = new PicController(picService);
        mockMvc = MockMvcBuilders.standaloneSetup(picController)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void findPicsTest() throws Exception {
        int picId = 1;
        Pic pic = new Pic();
        pic.setId(picId);
        ArrayList<Pic> picList = new ArrayList<>();
        picList.add(pic);

        when(picService.findAll()).thenReturn(picList);

        this.mockMvc.perform(get("/pic/pics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(picList.size())))
                .andExpect(jsonPath("$[0].id", is(picId)));

        verify(picService).findAll();
        verifyNoMoreInteractions(picService);
    }

    @Test
    void picsCountTest() throws Exception {
        Long count = 1L;
        when(picService.count()).thenReturn(count);

        this.mockMvc.perform(get("/pic/pics/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(count.toString()));

        verify(picService).count();
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPagePageAndSizeTest() throws Exception {
        int passedSize = 1;
        int passedPage = 0;
        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);
        ArrayList<Pic> picList = new ArrayList<>();
        picList.add(pic);

        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
        Page<Pic> pagePic = new PageImpl<>(picList, pageRequest, picList.size());

        Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
        when(picService.findAll(paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/pics_page?page=" + passedPage + "&size=" + passedSize))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(passedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findAll(paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPagePageAndSizeOutOfRangeTest() throws Exception {
        int passedSize = -1;
        int passedPage = -1;
        int fixedSize = 1;
        int fixedPage = 0;
        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);
        ArrayList<Pic> picList = new ArrayList<>();
        picList.add(pic);

        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(fixedPage, fixedSize, sortFinal);
        Page<Pic> pagePic = new PageImpl<>(picList, pageRequest, picList.size());

        Pageable paging = PageRequest.of(fixedPage, fixedSize, sortFinal);
        when(picService.findAll(paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/pics_page?page=" + passedPage + "&size=" + passedSize))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(fixedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findAll(paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPagePageAndSizeOutOfRangeTest2() throws Exception {
        int passedSize = 1000;
        int passedPage = 0;
        int fixedSize = 100;
        int picId = 1;

        ArrayList<Pic> picList = new ArrayList<>();
        for (int i = 0; i < fixedSize; i++) {
            Pic pic = new Pic();
            pic.setId(picId);
            picList.add(pic);
        }

        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(passedPage, fixedSize, sortFinal);
        Page<Pic> pagePic = new PageImpl<>(picList, pageRequest, picList.size());

        Pageable paging = PageRequest.of(passedPage, fixedSize, sortFinal);
        when(picService.findAll(paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/pics_page?page=" + passedPage + "&size=" + passedSize))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(fixedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findAll(paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPageSortAndDirTest() throws Exception {
        int passedPage = 0;

        ArrayList<Pic> picList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Pic pic = new Pic();
            pic.setEntityId(i);
            pic.setId(1-i);
            picList.add(pic);
        }

        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "entityId"));
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(passedPage, picList.size(), sortFinal);
        Page<Pic> pagePic = new PageImpl<>(picList, pageRequest, picList.size());

        Pageable paging = PageRequest.of(passedPage, picList.size(), sortFinal);
        when(picService.findAll(paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/pics_page?page=" + passedPage + "&size=" + picList.size() + "&sort=entityId&dir=asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(picList.size())))
                .andExpect(jsonPath("$.content[0].entityId", is(0)))
                .andExpect(jsonPath("$.content[0].id", is(1)));

        verify(picService).findAll(paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPageSortAndDirTest2() throws Exception {
        int passedPage = 0;

        ArrayList<Pic> picList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Pic pic = new Pic();
            pic.setEntityId(1-i);
            pic.setId(i);
            picList.add(pic);
        }

        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.DESC, "entityId"));
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(passedPage, picList.size(), sortFinal);
        Page<Pic> pagePic = new PageImpl<>(picList, pageRequest, picList.size());

        Pageable paging = PageRequest.of(passedPage, picList.size(), sortFinal);
        when(picService.findAll(paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/pics_page?page=" + passedPage + "&size=" + picList.size() + "&sort=entityId&dir=desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(picList.size())))
                .andExpect(jsonPath("$.content[0].entityId", is(1)))
                .andExpect(jsonPath("$.content[0].id", is(0)));

        verify(picService).findAll(paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPageFilterTest() throws Exception {
        int passedSize = 1;
        int passedPage = 0;
        String passedFilter = "test";

        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);
        ArrayList<Pic> picList = new ArrayList<>();
        picList.add(pic);

        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
        Page<Pic> pagePic = new PageImpl<>(picList, pageRequest, picList.size());

        Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
        when(picService.findByTitleContaining(passedFilter, paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/pics_page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(passedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findByTitleContaining(passedFilter, paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPageFilterEntityTypeTest() throws Exception {
        int passedSize = 1;
        int passedPage = 0;
        String passedFilter = "test";
        String passedEntityType = "entity";

        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);
        ArrayList<Pic> picList = new ArrayList<>();
        picList.add(pic);

        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
        Page<Pic> pagePic = new PageImpl<>(picList, pageRequest, picList.size());

        Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
        when(picService.findByEntityTypeAndTitleContaining(passedEntityType, passedFilter, paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/pics_page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter + "&entityType=" + passedEntityType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(passedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findByEntityTypeAndTitleContaining(passedEntityType, passedFilter, paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPageEntityTypeEntityIdTest() throws Exception {
        int passedSize = 1;
        int passedPage = 0;
        int passedEntityId = 2;
        String passedEntityType = "entity";

        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);
        ArrayList<Pic> picList = new ArrayList<>();
        picList.add(pic);

        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
        Page<Pic> pagePic = new PageImpl<>(picList, pageRequest, picList.size());

        Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
        when(picService.findByEntityTypeAndEntityId(passedEntityType, passedEntityId, paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/pics_page?page=" + passedPage + "&size=" + passedSize + "&entityId=" + passedEntityId + "&entityType=" + passedEntityType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(passedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findByEntityTypeAndEntityId(passedEntityType, passedEntityId, paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void savePicTest() throws Exception {
        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);

        CompletableFuture<Pic> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            completableFuture.complete(pic);
        });
        when(picService.save(pic)).thenReturn(completableFuture);

        this.mockMvc.perform(put("/pic/pic")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pic)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(picId)));

        verify(picService).save(pic);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void deletePicTest() throws Exception {
        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);

        when(picService.findById(picId)).thenReturn(Optional.of(pic));

        this.mockMvc.perform(delete("/pic/pics/del/1"))
                .andExpect(status().isOk());

        verify(picService).findById(picId);
        verify(picService).delete(pic);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void deletePicNotFoundTest() throws Exception {
        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);

        when(picService.findById(picId)).thenReturn(Optional.empty());

        this.mockMvc.perform(delete("/pic/pics/del/1"))
                .andExpect(status().isNotFound());

        verify(picService).findById(picId);
        verifyNoMoreInteractions(picService);
    }

}
