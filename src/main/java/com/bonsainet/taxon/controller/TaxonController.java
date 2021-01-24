package com.bonsainet.taxon.controller;

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

    @GetMapping("/taxa_page")
    public List<Taxon> findTaxaForPage(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // sanitise
        String sortBy = "fullName";
        String sortDir = "ASC";
        Sort sortClean;
        if (sort == null) sort = "";
        if (dir == null) dir = "";
        if (sort.equalsIgnoreCase("family")) {
            sortBy = "family";
        } else if (sort.equalsIgnoreCase("genus")) {
            sortBy = "genus";
        } else if (sort.equalsIgnoreCase("commonName")) {
            sortBy = "commonName";
        } else if (sort.equalsIgnoreCase("generalType")) {
            sortBy = "generalType";
        } else if (sort.equalsIgnoreCase("countBonsais")) {
            sortBy = "countBonsais";
        }
        //TODO: could add multiple sorts with param sort=field1,dir1&sort=field2,dir2 etc.
        /*
        List<Order> orders = new ArrayList<Order>();
        Order order1 = new Order(Sort.Direction.DESC, "commonName");
        orders.add(order1);
        Order order2 = new Order(Sort.Direction.ASC, "countBonsais");
        orders.add(order2);
        sortClean = Sort.by(orders));
         */
        if (dir.equalsIgnoreCase("DESC")) sortDir = "DESC";
        if (sortDir == "ASC") {
            sortClean = Sort.by(sortBy).ascending().and(Sort.by("fullName"));
        } else {
            sortClean = Sort.by(sortBy).descending().and(Sort.by("fullName"));
        }
        Pageable paging = PageRequest.of(page, size, sortClean);
        Page<Taxon> taxaResults;
        if (filter == null) {
            taxaResults = taxonService.findAll(paging);
        } else {
            taxaResults = taxonService.findByFullNameContaining(filter, paging);
        }
        List<Taxon> taxaList = taxaResults.getContent();
        long totalRecords = taxaResults.getTotalElements(); //TODO: pass this back as well
        int totalPages = taxaResults.getTotalPages();
        return taxaList;
    }

    @GetMapping("/taxa_page2")
    public Page<Taxon> findTaxaForPage2(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // sanitise
        String sortBy = "fullName";
        String sortDir = "ASC";
        Sort sortClean;
        if (sort == null) sort = "";
        if (dir == null) dir = "";
        if (sort.equalsIgnoreCase("family")) {
            sortBy = "family";
        } else if (sort.equalsIgnoreCase("genus")) {
            sortBy = "genus";
        } else if (sort.equalsIgnoreCase("commonName")) {
            sortBy = "commonName";
        } else if (sort.equalsIgnoreCase("generalType")) {
            sortBy = "generalType";
        } else if (sort.equalsIgnoreCase("countBonsais")) {
            sortBy = "countBonsais";
        }
        //TODO: could add multiple sorts with param sort=field1,dir1&sort=field2,dir2 etc.
        /*
        List<Order> orders = new ArrayList<Order>();
        Order order1 = new Order(Sort.Direction.DESC, "commonName");
        orders.add(order1);
        Order order2 = new Order(Sort.Direction.ASC, "countBonsais");
        orders.add(order2);
        sortClean = Sort.by(orders));
         */
        if (dir.equalsIgnoreCase("DESC")) sortDir = "DESC";
        if (sortDir == "ASC") {
            sortClean = Sort.by(sortBy).ascending().and(Sort.by("fullName"));
        } else {
            sortClean = Sort.by(sortBy).descending().and(Sort.by("fullName"));
        }
        Pageable paging = PageRequest.of(page, size, sortClean);
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
