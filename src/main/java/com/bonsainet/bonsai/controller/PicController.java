package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.EntityType;
import com.bonsainet.bonsai.model.Pic;
import com.bonsainet.bonsai.model.TaxonDTO;
import com.bonsainet.bonsai.service.IPicService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

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

  @GetMapping(
      value = "/pics_page",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<Pic> findPicsForPage(
      @RequestParam(required = false) EntityType entityType,
      @RequestParam(required = false) Integer entityId,
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) List<String> sort,
      @RequestParam(required = false) List<String> dir,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {

    List<String> toExclude = new ArrayList<>();
    String mainClass = TaxonDTO.class.getName();
    Pageable paging = GeneralControllerHelper.getPageableFromRequest(sort, dir, page, size,
        mainClass, toExclude, Optional.empty(), Optional.empty(), Optional.of("id"));

    Page<Pic> picResults;

    // this feels very clumsy
    if (entityType == null) {
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
  public ResponseEntity<byte[]> getImage(@RequestParam(required = true) Integer id)
      throws IOException {
    Optional<Pic> p = picService.findById(id);
    if (p.isPresent()) {
      return new ResponseEntity<>(p.get().getImage(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  // this is a temporary endpoint
  @GetMapping(
      value = "/update",
      produces = MediaType.IMAGE_JPEG_VALUE
  )
  public ResponseEntity<byte[]> updateImage(@RequestParam(required = true) Integer id)
      throws IOException {
    Optional<Pic> p = picService.findById(id);
    Future<Pic> futureSave = null;
    Pic pSaved = null;
    if (p.isPresent()) {
      futureSave = picService.save(p.get());
      return new ResponseEntity<>(p.get().getImage(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping(
      value = "/thumb",
      produces = MediaType.IMAGE_JPEG_VALUE
  )
  public ResponseEntity<byte[]> getImageThumb(@RequestParam(required = true) Integer id) {
    Optional<Pic> p = picService.findById(id);
    if (p.isPresent()) {
      // TODO: how do we check if the thumbnail is ready?
      //  Could set it to a default 'preparing...' image until its set properly
      try {
        byte[] thumbImg = p.get().getImageThumb();
        return new ResponseEntity<>(thumbImg, HttpStatus.OK);
      } catch (IOException ioe) {
        try {
          Future<Pic> picSaved = picService.save(p.get());
          Pic pp = picSaved.get();
          return new ResponseEntity<>(pp.getImageThumb(), HttpStatus.OK);
        } catch (Exception e) {
          e.printStackTrace();
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(path = "/pic")
  public ResponseEntity<Pic> setPic(@Valid @RequestParam("p") String ps,
      @Valid @NotBlank @RequestParam("file") Optional<MultipartFile> file) {

    ObjectMapper objectWriter = new ObjectMapper();

    //TODO: this should be automatable, surely?
    Pic p = null;
    try {
      p = objectWriter.readValue(ps, Pic.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    assert p != null;
    if (file.isPresent()) {
      String fileName = picService.storeFile(file.get());
      if (p.getId() != null) {
        Optional<Pic> oldPic = picService.findById(p.getId());
        if (oldPic.isPresent()) {
          oldPic.get().deleteImageIfExists();
        }
      }
      p.setFileName(fileName);
    }

    try {
      Future<Pic> futurePic = picService.save(p);
      return new ResponseEntity<>(futurePic.get(), HttpStatus.OK);
    } catch (InterruptedException | ExecutionException ie) {
      ie.printStackTrace();
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @DeleteMapping(path = "/del/{id}")
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
