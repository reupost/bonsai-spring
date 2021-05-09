package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.model.TaxonDTO;
import com.bonsainet.bonsai.service.IBonsaiService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
@RequestMapping("bonsai")
public class BonsaiController {

  // @Autowired
  private IBonsaiService bonsaiService;

  public BonsaiController(IBonsaiService bonsaiService) {
    this.bonsaiService = bonsaiService;
  }

  @GetMapping("/bonsais")
  public List<Bonsai> findBonsais() {
    return bonsaiService.findAll();
  }


  @GetMapping("/bonsais_page")
  public Page<Bonsai> findBonsaisForPage(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) List<String> sort,
      @RequestParam(required = false) List<String> dir,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    // TODO this is ok, but sorting by non-indexed fields could become a problem
    Field[] allFields = BonsaiDTO.class.getDeclaredFields();
    Field[] taxonFields = TaxonDTO.class.getDeclaredFields();
    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    if (sort != null) {
      for (int i = 0; i < sort.size(); i++) {
        String sortItem = sort.get(i);
        String[] sortItemSplit = sortItem.split("\\.");
        Sort.Direction sortDir = Sort.Direction.ASC;
        if (dir != null) {
          if (dir.size() > i) {
            if (dir.get(i).equalsIgnoreCase("DESC")) {
              sortDir = Sort.Direction.DESC;
            }
          }
        }
        List<Field> f = Arrays.stream(allFields).filter(field ->
            field.getName().equalsIgnoreCase(sortItem)).collect(Collectors.toList());
        if (!f.isEmpty()) {
          sortBy.add(new Sort.Order(sortDir, f.get(0).getName()));
        } else {
          //is it a taxon field?
          if (sortItemSplit[0].equalsIgnoreCase("taxon") && sortItemSplit.length == 2) {
            List<Field> ftaxon = Arrays.stream(taxonFields).filter(field ->
                field.getName().equalsIgnoreCase(sortItemSplit[1])).collect(Collectors.toList());
            if (!ftaxon.isEmpty()) {
              sortBy.add(new Sort.Order(sortDir, "taxon." + ftaxon.get(0).getName()));
            }
          }
        }
      }
    }
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "tag"));

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

    Page<Bonsai> bonsaiResults;
    if (filter == null) {
      bonsaiResults = bonsaiService.findAll(paging);
    } else {
      bonsaiResults = bonsaiService.findByNameContaining(filter, paging);
    }
    return bonsaiResults;
  }

  @GetMapping("/bonsais/count")
  public Long countBonsais() {
    return bonsaiService.count();
  }

  @PutMapping(path = "/bonsai")
  public Bonsai setBonsai(@Valid @RequestBody Bonsai t) throws InterruptedException {
    // sleep(1000);
    return bonsaiService.save(t);
  }

  @DeleteMapping(path = "/bonsais/del/{id}")
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
