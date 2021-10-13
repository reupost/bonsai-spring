package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.Taxon;

import com.bonsainet.bonsai.model.TaxonDTO;
import com.bonsainet.bonsai.service.ITaxonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("taxon")
public class TaxonController {

  private ITaxonService taxonService;

  public TaxonController(ITaxonService taxonService) {
    this.taxonService = taxonService;
  }

  @GetMapping(path = "/{id}")
  public Optional<Taxon> getTaxon(@PathVariable Integer id) {
    return taxonService.findById(id);
  }

  @GetMapping("/taxa")
  public List<Taxon> findTaxa() {
    return taxonService.findAll();
  }

  /**
   * Get a page of taxa.
   *
   * @param filter filter for full name or common name contains
   * @param sort   sort field list
   * @param dir    sort direction list (paired with sort fields)
   * @param page   page (0-indexed)
   * @param size   page size
   * @return
   */
  @GetMapping("/page")
  public Page<Taxon> findTaxaForPage(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) List<String> sort,
      @RequestParam(required = false) List<String> dir,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {

    List<String> toExclude = new ArrayList<>();
    String mainClass = TaxonDTO.class.getName();
    Pageable paging = GeneralControllerHelper.getPageableFromRequest(sort, dir, page, size,
        mainClass, toExclude, Optional.empty(), Optional.empty(), Optional.of("fullName"));

    Page<Taxon> taxaResults;
    if (filter == null || filter.equals("")) {
      taxaResults = taxonService.findAll(paging);
    } else {
      taxaResults = taxonService.findByFullNameOrCommonNameContaining(filter, paging);
    }
    return taxaResults;
  }

  @GetMapping("/count")
  public Long countTaxa() {
    return taxonService.count();
  }

  @PutMapping(path = "")
  public Taxon updateTaxon(@Valid @RequestBody Taxon t) throws InterruptedException {
    sleep(1000);
    return taxonService.save(t);
  }

  @PutMapping(path = "/dto")
  public TaxonDTO updateTaxon(@Valid @RequestBody TaxonDTO taxonDTO) {
    // sleep(1000);
    Taxon taxon = taxonService.toTaxon(taxonDTO);
    return taxonService.toDto(taxonService.save(taxon));
  }

  @PostMapping(path = "/dto")
  public TaxonDTO newTaxon(@Valid @RequestBody TaxonDTO taxonDTO) {
    return updateTaxon(taxonDTO);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Long> deleteTaxon(@PathVariable Integer id) {
    Optional<Taxon> t = taxonService.findById(id);
    if (t.isPresent()) {
      taxonService.delete(t.get());
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
