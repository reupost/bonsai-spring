package com.bonsainet.taxon.controller;

import com.bonsainet.taxon.model.BonsaiDTO;
import com.bonsainet.taxon.service.IBonsaiDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BonsaiDTOController {

  @Autowired
  private IBonsaiDTOService bonsaiDTOService;

  @GetMapping("/bonsaisdto")
  public List<BonsaiDTO> findBonsaisDTODetails() {
    return bonsaiDTOService.findAll();
  }


  /**
   * Retrieve page of bonsaiDTOs.
   *
   * @param filter search filter
   * @param sort   list of sort items
   * @param dir    list of sort directions, paired with sort items
   * @param page   result page (0-indexed)
   * @param size   page size
   * @return Page of BonsaiTaxonDTO objects
   */
  @GetMapping("/bonsaisdto_page")
  public Page<BonsaiDTO> findBonsaisDTODetailsForPage(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) List<String> sort,
      @RequestParam(required = false) List<String> dir,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    // sanitise parameters
    Field[] allFields = BonsaiDTO.class.getDeclaredFields();
    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    for (int i = 0; i < sort.size(); i++) {
      String sortItem = sort.get(i);
      Sort.Direction sortDir = Sort.Direction.ASC;
      if (dir.size() > i) {
        if (dir.get(i).equalsIgnoreCase("DESC")) {
          sortDir = Sort.Direction.DESC;
        }
      }
      List<Field> f = Arrays.stream(allFields).filter(field ->
          field.getName().equalsIgnoreCase(sortItem)).collect(Collectors.toList());
      if (!f.isEmpty()) {
        sortBy.add(new Sort.Order(sortDir, f.get(0).getName()));
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

    Page<BonsaiDTO> bonsaiFullResults;
    if (filter == null) {
      bonsaiFullResults = bonsaiDTOService.findAll(paging);
    } else {
      bonsaiFullResults = bonsaiDTOService.findByNameContaining(filter, paging);
    }
    return bonsaiFullResults;
  }

  @GetMapping("/bonsaisdto/count")
  public Long countBonsais() {
    return bonsaiDTOService.count();
  }

}
