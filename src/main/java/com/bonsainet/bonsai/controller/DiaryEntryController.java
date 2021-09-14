package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.DiaryEntry;
import com.bonsainet.bonsai.service.IDiaryEntryService;

import java.util.ArrayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("diaryEntry")
public class DiaryEntryController {

  // @Autowired
  private IDiaryEntryService diaryEntryService;

  public DiaryEntryController(IDiaryEntryService diaryEntryService) {
    this.diaryEntryService = diaryEntryService;
  }

  @GetMapping("/diaryEntries")
  public List<DiaryEntry> findDiaryEntries() {
    return diaryEntryService.findAll();
  }


  @GetMapping("/diaryEntries_page")
  public Page<DiaryEntry> findDiaryEntriesForPage(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) List<String> sort,
      @RequestParam(required = false) List<String> dir,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    List<String> toExclude = new ArrayList<>();
    String mainClass = DiaryEntry.class.getName();
    Pageable paging = GeneralControllerHelper.getPageableFromRequest(sort, dir, page, size,
        mainClass, toExclude, Optional.empty(), Optional.empty(), Optional.of("entryDate"));

    Page<DiaryEntry> diaryEntryResults;
    if (filter == null) {
      diaryEntryResults = diaryEntryService.findAll(paging);
    } else {
      diaryEntryResults = diaryEntryService.findByEntryTextContaining(filter, paging);
    }
    return diaryEntryResults;
  }

  @GetMapping("/diaryEntries/count")
  public Long countDiaryEntries() {
    return diaryEntryService.count();
  }

  @PutMapping(path = "/diaryEntry")
  public DiaryEntry setDiaryEntry(@Valid @RequestBody DiaryEntry t) {
    // sleep(1000);
    return diaryEntryService.save(t);
  }

  @DeleteMapping(path = "/diaryEntry/del/{id}")
  public ResponseEntity<Long> deleteDiaryEntry(@PathVariable Integer id) {
    Optional<DiaryEntry> t = diaryEntryService.findById(id);
    if (t.isPresent()) {
      diaryEntryService.delete(t.get());
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
