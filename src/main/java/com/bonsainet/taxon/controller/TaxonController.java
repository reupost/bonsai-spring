package com.bonsainet.taxon.controller;

import com.bonsainet.taxon.model.BonsaiDTO;
import com.bonsainet.taxon.model.Taxon;

import com.bonsainet.taxon.service.ITaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class TaxonController {

    @Autowired
    private ITaxonService taxonService;

    @GetMapping("/taxa")
    public List<Taxon> findTaxa() {
        List<Taxon> taxa = taxonService.findAll();
        return taxa;
    }

    /**
     * Get a page of taxa.
     *
     * @param filter    filter for full name contains
     * @param sort      sort field list
     * @param dir       sort direction list (paired with sort fields)
     * @param page      page (0-indexed)
     * @param size      page size
     * @return
     */
    @GetMapping("/taxa_page")
    public Page<Taxon> findTaxaForPage(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) List<String> sort,
            @RequestParam(required = false) List<String> dir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // sanitise
        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        for (int i = 0; i < sort.size(); i++) {
            String sortItem = sort.get(i);
            Sort.Direction sortDir = Sort.Direction.ASC;
            if (dir.size() > i) {
                if (dir.get(i).equalsIgnoreCase("DESC")) sortDir = Sort.Direction.DESC;
            }
            if (sortItem.equalsIgnoreCase("family")) {
                sortBy.add(new Sort.Order(sortDir,"family"));
            } else if (sortItem.equalsIgnoreCase("genus")) {
                sortBy.add(new Sort.Order(sortDir,"genus"));
            } else if (sortItem.equalsIgnoreCase("commonName")) {
                sortBy.add(new Sort.Order(sortDir,"commonName"));
            } else if (sortItem.equalsIgnoreCase("generalType")) {
                sortBy.add(new Sort.Order(sortDir,"generalType"));
            } else if (sortItem.equalsIgnoreCase("countBonsais")) {
                sortBy.add(new Sort.Order(sortDir,"countBonsais"));
            }
        }
        sortBy.add(new Sort.Order(Sort.Direction.ASC,"fullName"));

        Sort sortFinal = Sort.by(sortBy);
        if (size < 1) size = 1;
        if (size > 100) size = 100;
        if (page < 0) page = 0;
        Pageable paging = PageRequest.of(page, size, sortFinal);

        Page<Taxon> taxaResults;
        if (filter == null) {
            taxaResults = taxonService.findAll(paging);
        } else {
            taxaResults = taxonService.findByFullNameContaining(filter, paging);
        }
        return taxaResults;
    }

    @GetMapping("/taxa/count")
    public Long countTaxa() {
        return taxonService.count();
    }

    @PutMapping(path="/taxon")
    public Taxon setTaxon(@Valid @RequestBody Taxon t) throws InterruptedException {
        sleep(1000);
        return taxonService.save(t);
    }

    @DeleteMapping(path="/taxon/del/{id}")
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
