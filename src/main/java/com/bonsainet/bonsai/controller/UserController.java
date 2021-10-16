package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.model.UserDTO;
import com.bonsainet.bonsai.model.TaxonDTO;
import com.bonsainet.bonsai.service.IUserService;

import java.time.LocalDateTime;
import java.util.Collections;
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
@RequestMapping("user")
public class UserController {

  // @Autowired
  private IUserService userService;

  public UserController(IUserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/{id}")
  public Optional<User> getUser(@PathVariable Integer id) {
    return userService.findById(id);
  }

  @GetMapping("/users")
  public List<User> findUsers() {
    return userService.findAll();
  }


  @GetMapping("/page")
  public Page<User> findUsersForPage(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) List<String> sort,
      @RequestParam(required = false) List<String> dir,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    List<String> toExclude = Collections.singletonList("bonsaiDTO");
    String mainClass = UserDTO.class.getName();
    String childClass = TaxonDTO.class.getName();
    Pageable paging = GeneralControllerHelper.getPageableFromRequest(sort, dir, page, size,
        mainClass, toExclude,
        Optional.of(childClass), Optional.of("bonsai"), Optional.of("userName"));

    Page<User> userResults;
    if (filter == null || filter.length() == 0) {
      userResults = userService.findAll(paging);
    } else {
      userResults = userService.findByUserNameContaining(filter, paging);
    }
    return userResults;
  }

  @GetMapping("/count")
  public Long countUsers() {
    return userService.count();
  }

  @PutMapping(path = "")
  public User updateUser(@Valid @RequestBody User t) {
    // sleep(1000);
    return userService.save(t);
  }

  @PutMapping(path = "/dto")
  public UserDTO updateUser(@Valid @RequestBody UserDTO userDTO) {
    User user = userService.toUser(userDTO);
    if (user.getDateRegistered() == null) {
      user.setDateRegistered(LocalDateTime.now());
    }
    return userService.toDto(userService.save(user));
  }

  @PostMapping(path = "/dto")
  public UserDTO newUser(@Valid @RequestBody UserDTO userDTO) {
    return updateUser(userDTO);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Long> deleteUser(@PathVariable Integer id) {
    Optional<User> t = userService.findById(id);
    if (t.isPresent()) {
      userService.delete(t.get());
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
