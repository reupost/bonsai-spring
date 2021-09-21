package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.model.TaxonDTO;
import com.bonsainet.bonsai.service.IBonsaiService;

import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("bonsai")
public class BonsaiController {

  // @Autowired
  private IBonsaiService bonsaiService;

  public BonsaiController(IBonsaiService bonsaiService) {
    this.bonsaiService = bonsaiService;
  }

  @GetMapping(path = "/{id}")
  public Optional<Bonsai> getBonsai(@PathVariable Integer id) {
    return bonsaiService.findById(id);
  }

  @GetMapping("/bonsais")
  public List<Bonsai> findBonsais() {
    return bonsaiService.findAll();
  }


  @GetMapping("/page")
  public Page<Bonsai> findBonsaisForPage(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) List<String> sort,
      @RequestParam(required = false) List<String> dir,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    List<String> toExclude = Collections.singletonList("taxonDTO");
    String mainClass = BonsaiDTO.class.getName();
    String childClass = TaxonDTO.class.getName();
    Pageable paging = GeneralControllerHelper.getPageableFromRequest(sort, dir, page, size,
        mainClass, toExclude,
        Optional.of(childClass), Optional.of("taxon"), Optional.of("tag"));

    Page<Bonsai> bonsaiResults;
    if (filter == null || filter.length() == 0) {
      bonsaiResults = bonsaiService.findAll(paging);
    } else {
      // bonsaiResults = bonsaiService.findByNameContaining(filter, paging);
      bonsaiResults = bonsaiService.findByNameOrTaxonContaining(filter, paging);
    }
    return bonsaiResults;
  }

  @GetMapping("/count")
  public Long countBonsais() {
    return bonsaiService.count();
  }

  @PutMapping(path = "/")
  public Bonsai setBonsai(@Valid @RequestBody Bonsai t) {
    // sleep(1000);
    return bonsaiService.save(t);
  }

  @PutMapping(path = "/dto")
  public BonsaiDTO setBonsai(@Valid @RequestBody BonsaiDTO bonsaiDTO) {
    // sleep(1000);
    Bonsai bonsai = bonsaiService.toBonsai(bonsaiDTO);
    return bonsaiService.toDto(bonsaiService.save(bonsai));
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Long> deleteBonsai(@PathVariable Integer id) {
    Optional<Bonsai> t = bonsaiService.findById(id);
    if (t.isPresent()) {
      bonsaiService.delete(t.get());
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
