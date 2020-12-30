package com.bonsainet.species.controller;

import com.bonsainet.species.model.Taxon;
// import com.bonsainet.species.service.ITaxonService;

import com.bonsainet.species.service.ITaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TaxaNewController {

    @Autowired
    private ITaxonService taxonService;

    @GetMapping("/taxa")
    public List<Taxon> findTaxa(/* Model model */) {
        List<Taxon> taxa = taxonService.findAll();
        return taxa;
        // model.addAttribute("taxa", speciesAll);
        // return "showTaxa";
    }

    @GetMapping("/taxa_page")
    public List<Taxon> findTaxaForPage(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        Page<Taxon> taxaResults;
        if (filter == null) {
            taxaResults = taxonService.findAll(paging);
        } else {
            taxaResults = taxonService.findByFullNameContaining(filter, paging);
        }
        List<Taxon> taxaList = taxaResults.getContent();
        return taxaList;
        // model.addAttribute("taxa", speciesAll);
        // return "showTaxa";
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
