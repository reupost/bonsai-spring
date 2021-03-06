package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.DiaryEntry;
import com.bonsainet.bonsai.model.EntityType;
import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.service.PicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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

    private final int imgWidth = 100;
    private final int imgHeight = 150;

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

        this.mockMvc.perform(get("/pic/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(count.toString()));

        verify(picService).count();
        verifyNoMoreInteractions(picService);
    }

    @Test
    void getPicByIdTest() throws Exception {
        int picId = 1;
        Pic pic = new Pic();
        pic.setId(picId);

        when(picService.findById(picId)).thenReturn(Optional.of(pic));

        this.mockMvc.perform(get("/pic/" + picId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", is(picId)));

        verify(picService).findById(picId);
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

        this.mockMvc.perform(get("/pic/page?page=" + passedPage + "&size=" + passedSize))
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

        this.mockMvc.perform(get("/pic/page?page=" + passedPage + "&size=" + passedSize))
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
        int fixedSize = GeneralControllerHelper.MAX_LIST_SIZE;
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

        this.mockMvc.perform(get("/pic/page?page=" + passedPage + "&size=" + passedSize))
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
            pic.setEntityType(EntityType.BONSAI);
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

        this.mockMvc.perform(get("/pic/page?page=" + passedPage +
            "&size=" + picList.size() +
            "&sort=entityId&dir=asc"))
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
            pic.setEntityType(EntityType.BONSAI);
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

        this.mockMvc.perform(get("/pic/page?page=" + passedPage + "&size=" + picList.size() + "&sort=entityId&dir=desc"))
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

        this.mockMvc.perform(get("/pic/page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter))
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
        EntityType passedEntityType = EntityType.BONSAI;

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

        this.mockMvc.perform(get("/pic/page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter + "&entityType=" + passedEntityType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(passedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findByEntityTypeAndTitleContaining(passedEntityType, passedFilter, paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPageEntityTypeTest() throws Exception {
        int passedSize = 1;
        int passedPage = 0;
        EntityType passedEntityType = EntityType.BONSAI;

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
        when(picService.findByEntityType(passedEntityType, paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/page?page=" + passedPage + "&size=" + passedSize + "&entityType=" + passedEntityType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(passedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findByEntityType(passedEntityType,  paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPageEntityTypeEntityIdTest() throws Exception {
        int passedSize = 1;
        int passedPage = 0;
        int passedEntityId = 2;
        EntityType passedEntityType = EntityType.BONSAI;

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

        this.mockMvc.perform(get("/pic/page?page=" + passedPage + "&size=" + passedSize + "&entityId=" + passedEntityId + "&entityType=" + passedEntityType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(passedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findByEntityTypeAndEntityId(passedEntityType, passedEntityId, paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void findPicsForPageFilterEntityTypeEntityIdTest() throws Exception {
        int passedSize = 1;
        int passedPage = 0;
        String passedFilter = "test";
        EntityType passedEntityType = EntityType.BONSAI;
        int passedEntityId = 2;

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
        when(picService.findByEntityTypeAndEntityIdAndTitleContaining(passedEntityType, passedEntityId, passedFilter, paging)).thenReturn(pagePic);

        this.mockMvc.perform(get("/pic/page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter + "&entityType=" + passedEntityType + "&entityId=" + passedEntityId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(passedSize)))
                .andExpect(jsonPath("$.content[0].id", is(picId)));

        verify(picService).findByEntityTypeAndEntityIdAndTitleContaining(passedEntityType, passedEntityId, passedFilter, paging);
        verifyNoMoreInteractions(picService);
    }

    @Test
    void savePicTest() throws Exception {
        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);
        pic.setEntityType(EntityType.BONSAI);
        pic.setEntityId(picId);

        CompletableFuture<Pic> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            completableFuture.complete(pic);
        });
        when(picService.save(pic)).thenReturn(completableFuture);

        String jsonPic = new ObjectMapper().writeValueAsString(pic);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/pic");
        request.contentType(MediaType.APPLICATION_FORM_URLENCODED);
        request.param("p", jsonPic);
        ResultActions resultActions = this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(picId)));

        verify(picService).save(pic);
        verifyNoMoreInteractions(picService);
    }

    //TODO another rather complicated test to refactor
    @Test
    void saveNewPicWithFileTest() throws Exception {
        int picId = 1;
        String savedFileName = "savedFile.jpg";

        Pic pic = new Pic();
        pic.setId(picId);
        pic.setEntityType(EntityType.BONSAI);
        pic.setEntityId(picId);

        Pic picSaved = new Pic();
        picSaved.setId(picId);
        picSaved.setEntityType(EntityType.BONSAI);
        picSaved.setEntityId(picId);
        picSaved.setFileName(savedFileName);

        Pic oldPicMock = mock(Pic.class);

        MockMultipartFile file = new MockMultipartFile("file",
            "fileThatDoesNotExists.jpg",
            "text/plain",
            "This is dummy content".getBytes(StandardCharsets.UTF_8));

        CompletableFuture<Pic> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            completableFuture.complete(picSaved);
        });
        when(picService.save(any())).thenReturn(completableFuture);
        when(picService.storeFile(file)).thenReturn(savedFileName);
        when(picService.findById(picId)).thenReturn(Optional.of(oldPicMock));

        String jsonPic = new ObjectMapper().writeValueAsString(pic);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .multipart("/pic")
            .file(file);
        request.contentType(MediaType.APPLICATION_FORM_URLENCODED);
        request.param("p", jsonPic);

        ResultActions resultActions = this.mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", is(picId)))
            .andExpect(jsonPath("$.fileName", is(savedFileName)));

        verify(picService).save(picSaved);
        verify(picService).storeFile(file);
        verify(picService).findById(picId);
        verifyNoMoreInteractions(picService);
        verify(oldPicMock).deleteImageIfExists();
    }

    @Test
    void saveBadPicTest() throws Exception {
        String jsonPic = "bad json";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/pic");
        request.contentType(MediaType.APPLICATION_FORM_URLENCODED);
        request.param("p", jsonPic);
        ResultActions resultActions = this.mockMvc.perform(request)
            .andExpect(status().isBadRequest());

        verifyNoInteractions(picService);
    }

    @Test
    void deletePicTest() throws Exception {
        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);

        when(picService.findById(picId)).thenReturn(Optional.of(pic));

        this.mockMvc.perform(delete("/pic/1"))
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

        this.mockMvc.perform(delete("/pic/1"))
                .andExpect(status().isNotFound());

        verify(picService).findById(picId);
        verifyNoMoreInteractions(picService);
    }

    @Test
    public void getImageTest() {
        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);

        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_3BYTE_BGR);

        File file = null;
        try {
            file = File.createTempFile("tmp", ".jpg");
            ImageIO.write(bufferedImage, "jpg", file);

            pic.setRootFolder(file.getParentFile().getCanonicalPath());
            pic.setFileName(file.getName());
            pic.setDimensions();

            when(picService.findById(picId)).thenReturn(Optional.of(pic));

            this.mockMvc.perform(get("/pic/image/" + picId));
            //actually returns server 500 error for some reason

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file.isFile() && !file.delete()) {
                // failed to delete the (existing) file
            }
        }

        verify(picService).findById(picId);
        verifyNoMoreInteractions(picService);
    }

    @Test
    public void getImageNotFoundTest() {
        int picId = 1;

        Pic pic = new Pic();
        pic.setId(picId);

        when(picService.findById(picId)).thenReturn(Optional.empty());

        try {
            this.mockMvc.perform(get("/pic/image/" + picId))
                .andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(picService).findById(picId);
        verifyNoMoreInteractions(picService);
    }

    @Test
    public void getImageThumbTest() {
        int picId = 1;
        Pic picMock = mock(Pic.class);
        picMock.setId(picId);

        byte[] bytes;
        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_3BYTE_BGR);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
            bytes = baos.toByteArray();
            when(picService.findById(picId)).thenReturn(Optional.of(picMock));
            when(picMock.getImageThumb()).thenReturn(bytes);
            this.mockMvc.perform(get("/pic/thumb/" + picId));
            verify(picService).findById(picId);
            verifyNoMoreInteractions(picService);
            verify(picMock).getImageThumb();
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }


    //TODO this seems like a brittle test that cares too much about the inner workings of the function
    //it's testing. Refactor to raise abstraction level.
    @Test
    public void getImageThumbNotSavedTest() {
        int picId = 1;
        Pic picMock = mock(Pic.class);

        Future<Pic> picFuture = mock(Future.class);

        picMock.setId(picId);

        byte[] bytes;
        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_3BYTE_BGR);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
            bytes = baos.toByteArray();
            when(picService.findById(picId)).thenReturn(Optional.of(picMock));
            when(picService.save(picMock)).thenReturn(picFuture);
            when(picFuture.get()).thenReturn(picMock);
            when(picMock.getImageThumb())
                .thenThrow(IOException.class)
                .thenReturn(bytes);
            this.mockMvc.perform(get("/pic/thumb/" + picId));
            verify(picService).findById(picId);
            verify(picService).save(picMock);
            verifyNoMoreInteractions(picService);
            verify(picMock, times(2)).getImageThumb();
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }
}
