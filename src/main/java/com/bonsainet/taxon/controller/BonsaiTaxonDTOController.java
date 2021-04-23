package com.bonsainet.taxon.controller;

import com.bonsainet.taxon.model.BonsaiTaxonDTO;
import com.bonsainet.taxon.service.IBonsaiTaxonDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BonsaiTaxonDTOController {

    @Autowired
    private IBonsaiTaxonDTOService bonsaiTaxonDTOService;

    @GetMapping("/bonsaisfull")
    public List<BonsaiTaxonDTO> findBonsaisFullDetails() {
        List<BonsaiTaxonDTO> bonsaisFullDetails = bonsaiTaxonDTOService.findAll();
        return bonsaisFullDetails;
    }


    @GetMapping("/bonsaisfull_page")
    public Page<BonsaiTaxonDTO> findBonsaisFullDetailsForPage(
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
        if (sort.equalsIgnoreCase("tag")) {
            sortBy = "tag";
        } else if (sort.equalsIgnoreCase("dateAcquired")) {
            sortBy = "dateAcquired";
        } else if (sort.equalsIgnoreCase("yearStarted")) {
            sortBy = "yearStarted";
        } else if (sort.equalsIgnoreCase("source")) {
            sortBy = "source";
        } else if (sort.equalsIgnoreCase("style")) {
            sortBy = "style";
        } else if (sort.equalsIgnoreCase("stateWhenAcquired")) {
            sortBy = "stateWhenAcquired";
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
            sortClean = Sort.by(sortBy).ascending().and(Sort.by("name"));
        } else {
            sortClean = Sort.by(sortBy).descending().and(Sort.by("name"));
        }
        Pageable paging = PageRequest.of(page, size, sortClean);
        Page<BonsaiTaxonDTO> bonsaiFullResults;
        if (filter == null) {
            bonsaiFullResults = bonsaiTaxonDTOService.findAll(paging);
        } else {
            bonsaiFullResults = bonsaiTaxonDTOService.findByNameContaining(filter, paging);
        }
        return bonsaiFullResults;
    }

    @GetMapping("/bonsaisfull/count")
    public Long countBonsais() {
        return bonsaiTaxonDTOService.count();
    }

}
