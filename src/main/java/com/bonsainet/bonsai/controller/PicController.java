package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.service.IPicService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("pic")
public class PicController {

  private static final Logger logger = LoggerFactory.getLogger(PicController.class);

  @Value("${pic.rootfolder}")
  private String picRootFolder;

  // @Autowired
  private IPicService picService;

  public PicController(IPicService picService) {
    this.picService = picService;
  }

  @GetMapping("/pics")
  public List<Pic> findPics() {
    return picService.findAll();
  }

  @GetMapping("/pics_page")
  public Page<Pic> findPicsForPage(
      @RequestParam(required = false) String entityType,
      @RequestParam(required = false) Integer entityId,
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) List<String> sort,
      @RequestParam(required = false) List<String> dir,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {

    String[] picFieldNames = Stream.of(Pic.class.getDeclaredFields())
        .map(Field::getName)
        .toArray(String[]::new);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    if (sort != null) {
      for (int i = 0; i < sort.size(); i++) {
        String sortItem = sort.get(i);
        Sort.Direction sortDir = Sort.Direction.ASC;
        if (dir != null) {
          if (dir.size() > i) {
            if (dir.get(i).equalsIgnoreCase("DESC")) {
              sortDir = Sort.Direction.DESC;
            }
          }
        }
        List<String> f = Arrays.stream(picFieldNames)
            .filter(fieldName -> fieldName.equalsIgnoreCase(sortItem))
            .collect(Collectors.toList());
        if (!f.isEmpty()) {
          sortBy.add(new Sort.Order(sortDir, f.get(0)));
        }
      }
    }
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));

    Sort sortFinal = Sort.by(sortBy);
    if (size < 1) {
      size = 1;
    }
    if (size > 100) {
      size = 100;
    }
    if (page < 0) {
      page = 0;
    }
    Pageable paging = PageRequest.of(page, size, sortFinal);

    Page<Pic> picResults;

    // this feels very clumsy
    if (entityType == null || entityType.length() == 0) {
      if (filter == null || filter.length() == 0) {
        picResults = picService.findAll(paging);
      } else {
        picResults = picService.findByTitleContaining(filter, paging);
      }
    } else {
      if (entityId == null) {
        if (filter == null || filter.length() == 0) {
          picResults = picService.findByEntityType(entityType, paging);
        } else {
          picResults = picService.findByEntityTypeAndTitleContaining(entityType, filter, paging);
        }
      } else {
        if (filter == null || filter.length() == 0) {
          picResults = picService.findByEntityTypeAndEntityId(entityType, entityId, paging);
        } else {
          picResults = picService
              .findByEntityTypeAndEntityIdAndTitleContaining(entityType, entityId, filter, paging);
        }
      }
    }
    return picResults;
  }

  @GetMapping("/pics/count")
  public Long countPics() {
    return picService.count();
  }

  @GetMapping(
      value = "/image",
      produces = MediaType.IMAGE_JPEG_VALUE
  )
  public @ResponseBody byte[] getImage(@RequestParam(required = true) Integer id)
      throws IOException {
    Optional<Pic> p = picService.findById(id);
    if (p.isPresent()) {
      return p.get().getImage();
    } else {
      return null;
    }
  }

  // this is a temporary endpoint
  @GetMapping(
      value = "/update",
      produces = MediaType.IMAGE_JPEG_VALUE
  )
  public @ResponseBody byte[] updateImage(@RequestParam(required = true) Integer id)
      throws IOException {
    Optional<Pic> p = picService.findById(id);
    Future<Pic> futureSave = null;
    Pic pSaved = null;
    if (p.isPresent()) {
      try {
        futureSave = picService.save(p.get());
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
      // this is needed if we want the thumb via getImageThumb(), since it might not be generated yet,
      // but the full image will definitely be there already in the filesystem.
//      try {
//        pSaved = futureSave.get();
//      } catch (InterruptedException | ExecutionException e) {
//
//      }
//      return pSaved.getImageThumb();
      return p.get().getImage();
    } else {
      return null;
    }
  }

  @GetMapping(
      value = "/thumb",
      produces = MediaType.IMAGE_JPEG_VALUE
  )
  public @ResponseBody byte[] getImageThumb(@RequestParam(required = true) Integer id)
      throws IOException {
    Optional<Pic> p = picService.findById(id);
    if (p.isPresent()) {
      // TODO: how do we check if the thumbnail is ready?
      //  Could set it to a default 'preparing...' image until its set properly
      return p.get().getImageThumb();
    } else {
      return null;
    }
  }

  @PutMapping(path = "/pic")
  public Pic setPic(@Valid @RequestBody Pic p) {
    Future<Pic> futurePic = null;
    try {
      futurePic = picService.save(p);
      return futurePic.get();
    } catch (InterruptedException | ExecutionException ie) {
      ie.printStackTrace();
    }
    return null;
  }

  @DeleteMapping(path = "/pics/del/{id}")
  public ResponseEntity<Long> deletePic(@PathVariable Integer id) {
    Optional<Pic> t = picService.findById(id);
    if (t.isPresent()) {
      picService.delete(t.get());
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
