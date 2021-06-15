package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.service.IBonsaiDTOService;
import java.util.Collections;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("bonsai")
public class BonsaiDTOController {

  //@Autowired
  private IBonsaiDTOService bonsaiDTOService;

  public BonsaiDTOController(IBonsaiDTOService bonsaiDtoService) {
    this.bonsaiDTOService = bonsaiDtoService;
  }

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

    List<String> toExclude = Collections.singletonList("taxonDTO");
    Pageable paging = GeneralControllerHelper.getPageableFromRequest(sort, dir, page, size,
        "com.bonsainet.bonsai.model.BonsaiDTO", toExclude,
        Optional.of("com.bonsainet.bonsai.model.TaxonDTO"), Optional.of("taxon"), Optional.of("tag"));

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
