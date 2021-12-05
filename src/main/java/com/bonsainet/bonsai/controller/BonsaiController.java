package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.Bonsai;
import com.bonsainet.bonsai.model.BonsaiDTO;
import com.bonsainet.bonsai.model.Taxon;
import com.bonsainet.bonsai.model.TaxonDTO;
import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.service.IBonsaiService;

import com.bonsainet.bonsai.service.ITaxonService;
import com.bonsainet.bonsai.service.IUserService;
import com.bonsainet.bonsai.specs.BonsaiSpecification;
import com.bonsainet.bonsai.specs.SearchCriteria;
import com.bonsainet.bonsai.specs.SearchOperation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.apache.tomcat.jni.Local;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("bonsai")
public class BonsaiController {

  private IBonsaiService bonsaiService;
  private IUserService userService;
  private ITaxonService taxonService;

  public BonsaiController(IBonsaiService bonsaiService, IUserService userService,
      ITaxonService taxonService) {
    this.bonsaiService = bonsaiService;
    this.userService = userService;
    this.taxonService = taxonService;
  }

  @GetMapping(path = "/{id}")
  public Optional<Bonsai> getBonsai(@PathVariable Integer id) {
    return bonsaiService.findById(id);
  }

  @GetMapping("/bonsais")
  public List<Bonsai> findBonsais(@RequestParam(required = false) Integer userId) {
    if (userId == null) {
      return bonsaiService.findAll();
    } else {
      Optional<User> optionalUser = userService.findById(userId);
      if (optionalUser.isPresent()) {
        return bonsaiService.findAll(optionalUser.get());
      } else {
        return Collections.emptyList();
      }
    }
  }

  @GetMapping("/find")
  public List<Bonsai> findBonsaisSpecification(@RequestParam String columnFilters) {
    Specification<Bonsai> specBonsai = new BonsaiSpecification();
    try {
      JSONObject jsonObject = new JSONObject(columnFilters.trim());
      Iterator<String> keys = jsonObject.keys();
      while(keys.hasNext()) {
        String key = keys.next();
        if (jsonObject.get(key) instanceof String || jsonObject.get(key) instanceof Number) {
          if (key.equalsIgnoreCase("genus")) { //special case
            specBonsai = specBonsai.and(BonsaiSpecification.forGenus(jsonObject.get(key).toString()));
          } else {
            try {
              Field field = Bonsai.class.getDeclaredField(key);
              Type type = field.getType();
              BonsaiSpecification bs = new BonsaiSpecification();
              if (type.equals(LocalDate.class)) {
                LocalDate localDate = LocalDate.parse(jsonObject.get(key).toString());
                bs.add(new SearchCriteria(key, localDate, SearchOperation.EQUAL));
              } else {
                bs.add(new SearchCriteria(key, jsonObject.get(key), SearchOperation.EQUAL));
              }
              specBonsai = specBonsai.and(bs);
            } catch (NoSuchFieldException | DateTimeParseException e) {
              //do nothing, i.e. ignore
            }
          }
        }
      }
    } catch (JSONException e) {
      //TODO: log this?
      return null;
    }

    return bonsaiService.findAll(specBonsai);
  }

  //TODO should possibly refactor to allow any combination of filtering parameters
  @GetMapping("/page")
  public Page<Bonsai> findBonsaisForPage(
      @RequestParam(required = false) Integer userId,
      @RequestParam(required = false) Integer taxonId,
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) List<String> sort,
      @RequestParam(required = false) List<String> dir,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    List<String> toExclude = Collections.singletonList("taxonDTO");
    String mainClass = BonsaiDTO.class.getName();
    String childClass = TaxonDTO.class.getName();
    Pageable paging = GeneralControllerHelper.getPageableFromRequest(sort, dir, page, size,
        mainClass, toExclude,
        Optional.of(childClass), Optional.of("taxon"), Optional.of("tag"));

    Page<Bonsai> bonsaiResults;
    if (taxonId == null) {
      if (userId == null) {
        if (filter == null || filter.length() == 0) {
          bonsaiResults = bonsaiService.findAll(paging);
        } else {
          bonsaiResults = bonsaiService.findByNameOrTaxonContaining(filter, paging);
        }
      } else {
        Optional<User> optionalUser = userService.findById(userId);
        if (optionalUser.isPresent()) {
          if (filter == null || filter.length() == 0) {
            bonsaiResults = bonsaiService.findAll(optionalUser.get(), paging);
          } else {
            bonsaiResults = bonsaiService.findByNameOrTaxonContaining(optionalUser.get(),
                filter, paging);
          }
        } else {
          bonsaiResults = Page.empty();
        }
      }
    } else {
      Optional<Taxon> optionalTaxon = taxonService.findById(taxonId);
      if (optionalTaxon.isPresent()) {
        if (userId == null) {
          if (filter == null || filter.length() == 0) {
            bonsaiResults = bonsaiService.findByTaxon(optionalTaxon.get(), paging);
          } else {
            bonsaiResults = bonsaiService.findByNameOrTaxonContaining(filter,
                paging); //ignore taxon?
          }
        } else {
          Optional<User> optionalUser = userService.findById(userId);
          if (optionalUser.isPresent()) {
            if (filter == null || filter.length() == 0) {
              bonsaiResults = bonsaiService.findByTaxon(optionalUser.get(), optionalTaxon.get(),
                  paging);
            } else {
              bonsaiResults = bonsaiService.findByNameOrTaxonContaining(optionalUser.get(),
                  filter, paging);
            }
          } else {
            bonsaiResults = Page.empty();
          }
        }
      } else {
        bonsaiResults = Page.empty();
      }
    }
    return bonsaiResults;
  }

  @GetMapping("/count")
  public Long countBonsais(@RequestParam(required = false) Integer userId) {
    if (userId == null) {
      return bonsaiService.count();
    } else {
      Optional<User> optionalUser = userService.findById(userId);
      if (optionalUser.isPresent()) {
        return bonsaiService.count(optionalUser.get());
      } else {
        return 0L;
      }
    }
  }

  @PutMapping(path = "")
  public Bonsai updateBonsai(@Valid @RequestBody Bonsai t) {
    // sleep(1000);
    return bonsaiService.save(t);
  }

  @PutMapping(path = "/dto")
  public BonsaiDTO updateBonsai(@Valid @RequestBody BonsaiDTO bonsaiDTO) {
    // sleep(1000);
    Bonsai bonsai = bonsaiService.toBonsai(bonsaiDTO);
    return bonsaiService.toDto(bonsaiService.save(bonsai));
  }

  @PostMapping(path = "/dto")
  public BonsaiDTO newBonsai(@Valid @RequestBody BonsaiDTO bonsaiDTO) {
    return updateBonsai(bonsaiDTO);
  }

  @DeleteMapping(path = "/{id}")
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
