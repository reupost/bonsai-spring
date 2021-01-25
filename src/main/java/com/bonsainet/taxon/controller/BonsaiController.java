package com.bonsainet.taxon.controller;

import com.bonsainet.taxon.model.Bonsai;

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
    public Page<Bonsai> findBonsaisForPage2(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // sanitise
        String sortBy = "name";
        String sortDir = "ASC";
        Sort sortClean;
        if (sort == null) sort = "";
        if (dir == null) dir = "";
//        if (sort.equalsIgnoreCase("family")) {
//            sortBy = "family";
//        } else if (sort.equalsIgnoreCase("genus")) {
//            sortBy = "genus";
//        } else if (sort.equalsIgnoreCase("commonName")) {
//            sortBy = "commonName";
//        } else if (sort.equalsIgnoreCase("generalType")) {
//            sortBy = "generalType";
//        } else if (sort.equalsIgnoreCase("countBonsais")) {
//            sortBy = "countBonsais";
//        }
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
            sortClean = Sort.by(sortBy).ascending().and(Sort.by("name"));
        } else {
            sortClean = Sort.by(sortBy).descending().and(Sort.by("name"));
        }
        Pageable paging = PageRequest.of(page, size, sortClean);
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
