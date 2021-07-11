package com.bonsainet.bonsai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.bonsainet.bonsai.model.EntityTypes;
import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.repository.PicRepository;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

//TODO: refactor test image creation and deletion into reusable class?

class PicServiceTest {

    private String tmpRootFolder = System.getProperty("java.io.tmpdir");

    private PicService picService;
    private ApplicationContext applicationContext;

    PicRepository picRepository;

    @BeforeEach
    void setup() {
        applicationContext = mock(ApplicationContext.class);
        picRepository = mock(PicRepository.class);
        picService = new PicService(applicationContext, picRepository);
        ReflectionTestUtils.setField(picService, "picRootFolder", tmpRootFolder);
    }

    @Test
    void shouldThrowExceptionWhenSavePicIsNull() {
        assertThrows(NullPointerException.class, () -> picService.save(null));
    }

    @Test
    void shouldSavePic() throws IllegalAccessException {
        Pic picMock = mock(Pic.class);
        when(picMock.getId()).thenReturn(1);
        when(picMock.getFileName()).thenReturn("test.jpg");
        when(picMock.getEntityId()).thenReturn(1);
        when(picMock.getEntityType()).thenReturn(EntityTypes.BONSAI);

        when(picService.findById(picMock.getId())).thenReturn(Optional.of(picMock));

        try {
            Future<Pic> picFuture = picService.save(picMock);
            picFuture.get(); //wait
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(picRepository).save(picMock);
        verify(picRepository).findById(picMock.getId());
        verifyNoMoreInteractions(picRepository);
        verify(picMock).supplementWith(picMock);
    }

    @Test
    void shouldFindAllPics() {
        picService.findAll();

        verify(picRepository).findAll();
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void shouldFindAllPicsPageable() {
        ArrayList<Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        Pageable paging = PageRequest.of(0, 1, sortFinal);

        picService.findAll(paging);

        verify(picRepository).findAll(paging);
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void shouldDeletePic() {
        Pic pic = new Pic();
        pic.setId(1);

        picService.delete(pic);

        verify(picRepository).delete(pic);
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void shouldFindByIdPic() {
        picService.findById(1);

        verify(picRepository).findById(1);
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void shouldCountPic() {
        picService.count();

        verify(picRepository).count();
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void shouldFindByEntityTypeAndEntityId() {
        ArrayList<Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        Pageable paging = PageRequest.of(0, 1, sortFinal);

        picService.findByEntityTypeAndEntityId(EntityTypes.BONSAI, 1, paging);

        verify(picRepository).findByEntityTypeAndEntityId(EntityTypes.BONSAI, 1, paging);
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void shouldFindByEntityType() {
        ArrayList<Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        Pageable paging = PageRequest.of(0, 1, sortFinal);

        picService.findByEntityType(EntityTypes.BONSAI, paging);

        verify(picRepository).findByEntityType(EntityTypes.BONSAI, paging);
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void shouldFindByEntityTypeAndTitleContaining() {
        ArrayList<Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        Pageable paging = PageRequest.of(0, 1, sortFinal);

        picService.findByEntityTypeAndTitleContaining(EntityTypes.BONSAI, "title", paging);

        verify(picRepository).findByEntityTypeAndTitleContaining(EntityTypes.BONSAI, "title", paging);
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void shouldFindByEntityTypeAndEntityIdAndTitleContaining() {
        ArrayList<Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        Pageable paging = PageRequest.of(0, 1, sortFinal);

        picService.findByEntityTypeAndEntityIdAndTitleContaining(EntityTypes.BONSAI, 1, "title", paging);

        verify(picRepository).findByEntityTypeAndEntityIdAndTitleContaining(EntityTypes.BONSAI, 1, "title", paging);
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void shouldFindByTitleContaining() {
        ArrayList<Order> sortBy = new ArrayList<>();
        sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sortFinal = Sort.by(sortBy);

        Pageable paging = PageRequest.of(0, 1, sortFinal);

        picService.findByTitleContaining("title", paging);

        verify(picRepository).findByTitleContaining( "title", paging);
        verifyNoMoreInteractions(picRepository);
    }

    @Test
    void storeFileTest() {
        String fileExtension = ".jpg";
        MockMultipartFile file = new MockMultipartFile("file",
            "fileThatDoesNotExists" + fileExtension,
            "text/plain",
            "This is dummy content".getBytes(StandardCharsets.UTF_8));

        String savedFilename = picService.storeFile(file);

        String savedFileExtension = "";
        try {
            savedFileExtension = savedFilename.substring(savedFilename.lastIndexOf("."));
        } catch(Exception e) {
            savedFileExtension = "";
        }

        assertNotNull(savedFilename);
        assertEquals(fileExtension, savedFileExtension);

        //tidy up
        File fileToDel = new File(tmpRootFolder + File.separatorChar, savedFilename);
        fileToDel.delete();
    }
}
