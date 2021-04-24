package com.bonsainet.taxon.controller;

import com.bonsainet.taxon.model.BonsaiDTO;
import com.bonsainet.taxon.service.IBonsaiDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BonsaiDTOController {

    @Autowired
    private IBonsaiDTOService bonsaiTaxonDTOService;

    @GetMapping("/bonsaisdto")
    public List<BonsaiDTO> findBonsaisDTODetails() {
        List<BonsaiDTO> bonsaisFullDetails = bonsaiTaxonDTOService.findAll();
        return bonsaisFullDetails;
    }


    /**
     * Retrieve page of bonsaiDTOs.
     *
     * @param filter    search filter
     * @param sort      list of sort items
     * @param dir       list of sort directions, paired with sort items
     * @param page      result page (0-indexed)
     * @param size      page size
     * @return      Page of BonsaiTaxonDTO objects
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
        ArrayList<Sort.Order> sortBy = new ArrayList<>();
        for (int i = 0; i < sort.size(); i++) {
            String sortItem = sort.get(i);
            Sort.Direction sortDir = Sort.Direction.ASC;
            if (dir.size() > i) {
                if (dir.get(i).equalsIgnoreCase("DESC")) sortDir = Sort.Direction.DESC;
            }
            if (sortItem.equalsIgnoreCase("tag")) {
                sortBy.add(new Sort.Order(sortDir,"tag"));
            } else if (sortItem.equalsIgnoreCase("taxonFullName")) {
                sortBy.add(new Sort.Order(sortDir,"taxonFullName"));
            } else if (sortItem.equalsIgnoreCase("taxonFamily")) {
                sortBy.add(new Sort.Order(sortDir,"taxonFamily"));
                sortBy.add(new Sort.Order(sortDir,"taxonFullName"));
            } else if (sortItem.equalsIgnoreCase("dateAcquired")) {
                sortBy.add(new Sort.Order(sortDir,"dateAcquired"));
            } else if (sortItem.equalsIgnoreCase("yearStarted")) {
                sortBy.add(new Sort.Order(sortDir,"yearStarted"));
            } else if (sortItem.equalsIgnoreCase("source")) {
                sortBy.add(new Sort.Order(sortDir,"source"));
            } else if (sortItem.equalsIgnoreCase("style")) {
                sortBy.add(new Sort.Order(sortDir,"style"));
            } else if (sortItem.equalsIgnoreCase("stateWhenAcquired")) {
                sortBy.add(new Sort.Order(sortDir,"stateWhenAcquired"));
            }
        }
        sortBy.add(new Sort.Order(Sort.Direction.ASC,"tag"));

        Sort sortFinal = Sort.by(sortBy);
        Pageable paging = PageRequest.of(page, size, sortFinal);

        Page<BonsaiDTO> bonsaiFullResults;
        if (filter == null) {
            bonsaiFullResults = bonsaiTaxonDTOService.findAll(paging);
        } else {
            bonsaiFullResults = bonsaiTaxonDTOService.findByNameContaining(filter, paging);
        }
        return bonsaiFullResults;
    }

    @GetMapping("/bonsaisdto/count")
    public Long countBonsais() {
        return bonsaiTaxonDTOService.count();
    }

}
