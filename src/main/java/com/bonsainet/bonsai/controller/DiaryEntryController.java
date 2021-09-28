package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.DiaryEntry;
import com.bonsainet.bonsai.model.DiaryEntryDTO;
import com.bonsainet.bonsai.service.IDiaryEntryService;

import java.time.LocalDateTime;
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

  @GetMapping(path = "/{id}")
  public Optional<DiaryEntry> getDiaryEntry(@PathVariable Integer id) {
    return diaryEntryService.findById(id);
  }

  @GetMapping("/diaryEntries")
  public List<DiaryEntry> findDiaryEntries() {
    return diaryEntryService.findAll();
  }


  @GetMapping("/page")
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

  @GetMapping("/count")
  public Long countDiaryEntries() {
    return diaryEntryService.count();
  }

  @PutMapping(path = "")
  public DiaryEntry updateDiaryEntry(@Valid @RequestBody DiaryEntry diaryEntry) {
    // sleep(1000);
    if (diaryEntry.getEntryDate() == null) {
      diaryEntry.setEntryDate(LocalDateTime.now());
    }
    return diaryEntryService.save(diaryEntry);
  }

  @PutMapping(path = "/dto")
  public DiaryEntryDTO updateDiaryEntry(@Valid @RequestBody DiaryEntryDTO diaryEntryDTO) {
    // sleep(1000);
    DiaryEntry diaryEntry = diaryEntryService.toDiaryEntry(diaryEntryDTO);
    if (diaryEntry.getEntryDate() == null) {
      diaryEntry.setEntryDate(LocalDateTime.now());
    }
    //TODO - do we update the entryDate, or leave it?
    return diaryEntryService.toDto(diaryEntryService.save(diaryEntry));
  }

  @PostMapping(path = "/dto")
  public DiaryEntryDTO newDiaryEntry(@Valid @RequestBody DiaryEntryDTO diaryEntryDTO) {
    return updateDiaryEntry(diaryEntryDTO);
  }

  @DeleteMapping(path = "/{id}")
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
