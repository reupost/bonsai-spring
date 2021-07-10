package com.bonsainet.bonsai.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.bonsainet.bonsai.model.EntityTypes;
import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.repository.PicRepository;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import javax.imageio.ImageIO;

//TODO get application context
//TODO: refactor test image creation and deletion into reusable class?

class PicServiceTest {

    private PicService picService;
    private ApplicationContext applicationContext;

    PicRepository picRepository;

    @BeforeEach
    void setup() {
        applicationContext = mock(ApplicationContext.class);
        picRepository = mock(PicRepository.class);
        picService = new PicService(applicationContext, picRepository);
    }

    @Test
    void shouldThrowExceptionWhenSavePicIsNull() {
        assertThrows(NullPointerException.class, () -> picService.save(null));
    }

    @Test
    void shouldSavePic() {
        Pic pic = new Pic();
        pic.setId(1);
        pic.setEntityId(1);
        pic.setEntityType(EntityTypes.BONSAI);
        final int imgWidth = 100;
        final int imgHeight = 150;
        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_3BYTE_BGR);

        File file = null;
        try {
            file = File.createTempFile("tmp", ".jpg");
            ImageIO.write(bufferedImage, "jpg", file);

            pic.setRootFolder(file.getParentFile().getCanonicalPath());
            pic.setFileName(file.getName());

            Future<Pic> picFuture = picService.save(pic);

            picFuture.get(); //wait

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file.isFile() && !file.delete()) {
                // failed to delete the (existing) file
            }
        }

        verify(picRepository).save(pic);
        verify(picRepository).findById(pic.getId());

        verifyNoMoreInteractions(picRepository);
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
}
