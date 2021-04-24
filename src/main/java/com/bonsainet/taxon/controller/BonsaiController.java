package com.bonsainet.taxon.controller;

import com.bonsainet.taxon.model.Bonsai;

import com.bonsainet.taxon.model.Taxon;
import com.bonsainet.taxon.service.IBonsaiService;
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
public class BonsaiController {

    @Autowired
    private IBonsaiService bonsaiService;

    @GetMapping("/bonsais")
    public List<Bonsai> findBonsais() {
        List<Bonsai> bonsais = bonsaiService.findAll();
        return bonsais;
    }


    @GetMapping("/bonsais_page")
    public Page<Bonsai> findBonsaisForPage(
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
            if (sortItem.equalsIgnoreCase("tag")) {
                sortBy.add(new Sort.Order(sortDir, "tag"));
            } else if (sortItem.equalsIgnoreCase("dateAcquired")) {
                sortBy.add(new Sort.Order(sortDir, "dateAcquired"));
            } else if (sortItem.equalsIgnoreCase("yearStarted")) {
                sortBy.add(new Sort.Order(sortDir, "yearStarted"));
            } else if (sortItem.equalsIgnoreCase("source")) {
                sortBy.add(new Sort.Order(sortDir, "source"));
            } else if (sortItem.equalsIgnoreCase("style")) {
                sortBy.add(new Sort.Order(sortDir, "style"));
            } else if (sortItem.equalsIgnoreCase("stateWhenAcquired")) {
                sortBy.add(new Sort.Order(sortDir, "stateWhenAcquired"));
            }
        }
        sortBy.add(new Sort.Order(Sort.Direction.ASC,"tag"));

        Sort sortFinal = Sort.by(sortBy);
        if (size < 1) size = 1;
        if (size > 100) size = 100;
        if (page < 0) page = 0;
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

    @PutMapping(path="/bonsai")
    public Bonsai setBonsai(@Valid @RequestBody Bonsai t) throws InterruptedException {
        sleep(1000);
        return bonsaiService.save(t);
    }

    @DeleteMapping(path="/bonsais/del/{id}")
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
