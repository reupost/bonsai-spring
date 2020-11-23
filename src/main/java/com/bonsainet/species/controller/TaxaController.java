package com.bonsainet.species.controller;

import com.bonsainet.species.Taxa;
import com.bonsainet.species.model.Taxon;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TaxaController {

    Taxa spAll;

    public TaxaController() {
        this.spAll = new Taxa();
    }


    @RequestMapping(method = RequestMethod.GET, path = "/speciesArray")
    public ArrayList<Taxon> getSpeciesArray() {
        return spAll.taxonArray;
    }


    /* @RequestMapping(method = RequestMethod.PUT, path = "/species")
    public String setSpecies(HttpServletRequest request) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(request.getQueryString());
            //Species sp = (Species) obj;
            //return this.spAll.addSpecies(sp);
        } catch (ParseException pe) {
            System.out.println("JSON parse error at position: " + pe.getPosition());
            System.out.println(pe);
        }

        //return this.spAll.addSpecies(sp);
        return "";
    } */

    @PutMapping(path="/species")
    public Taxon setTaxon(@Valid @RequestBody Taxon sp) throws InterruptedException {
        sleep(1000);
        return this.spAll.addTaxon(sp);
    }
}
