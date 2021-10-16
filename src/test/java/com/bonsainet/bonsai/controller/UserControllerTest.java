package com.bonsainet.bonsai.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.service.UserService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = User.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @BeforeEach
  void setup() {
    UserController userController = new UserController(userService);
    mockMvc = MockMvcBuilders.standaloneSetup(userController)
        .setMessageConverters(jacksonMessageConverter)
        .build();
  }

  @Test
  void findUsersTest() throws Exception {
    int userId = 1;
    User user = new User();
    user.setId(userId);
    ArrayList<User> userList = new ArrayList<>();
    userList.add(user);

    when(userService.findAll()).thenReturn(userList);

    this.mockMvc.perform(get("/user/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(userList.size())))
        .andExpect(jsonPath("$[0].id", is(userId)));

    verify(userService).findAll();
    verifyNoMoreInteractions(userService);
  }

  @Test
  void getUserByIdTest() throws Exception {
    int userId = 1;
    User user = new User();
    user.setId(userId);

    when(userService.findById(userId)).thenReturn(Optional.of(user));

    this.mockMvc.perform(get("/user/" + userId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", is(userId)));

    verify(userService).findById(userId);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void usersCountTest() throws Exception {
    Long count = 1L;
    when(userService.count()).thenReturn(count);

    this.mockMvc.perform(get("/user/count"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(count.toString()));

    verify(userService).count();
    verifyNoMoreInteractions(userService);
  }

  @Test
  void findUsersForPagePageAndSizeTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    int userId = 1;

    User user = new User();
    user.setId(userId);
    ArrayList<User> userList = new ArrayList<>();
    userList.add(user);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "userName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<User> pageUser = new PageImpl<>(userList, pageRequest, userList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(userService.findAll(paging)).thenReturn(pageUser);

    this.mockMvc.perform(get("/user/page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(userId)));

    verify(userService).findAll(paging);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void findUsersForPagePageAndSizeOutOfRangeTest() throws Exception {
    int passedSize = -1;
    int passedPage = -1;
    int fixedSize = 1;
    int fixedPage = 0;
    int userId = 1;

    User user = new User();
    user.setId(userId);
    ArrayList<User> userList = new ArrayList<>();
    userList.add(user);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "userName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(fixedPage, fixedSize, sortFinal);
    Page<User> pageUser = new PageImpl<>(userList, pageRequest, userList.size());

    Pageable paging = PageRequest.of(fixedPage, fixedSize, sortFinal);
    when(userService.findAll(paging)).thenReturn(pageUser);

    this.mockMvc.perform(get("/user/page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(userId)));

    verify(userService).findAll(paging);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void findUsersForPagePageAndSizeOutOfRangeTest2() throws Exception {
    int passedSize = 1000;
    int passedPage = 0;
    int fixedSize = GeneralControllerHelper.MAX_LIST_SIZE;
    int userId = 1;

    ArrayList<User> userList = new ArrayList<>();
    for (int i = 0; i < fixedSize; i++) {
      User user = new User();
      user.setId(userId);
      userList.add(user);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "userName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, fixedSize, sortFinal);
    Page<User> pageUser = new PageImpl<>(userList, pageRequest, userList.size());

    Pageable paging = PageRequest.of(passedPage, fixedSize, sortFinal);
    when(userService.findAll(paging)).thenReturn(pageUser);

    this.mockMvc.perform(get("/user/page?page=" + passedPage + "&size=" + passedSize))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(fixedSize)))
        .andExpect(jsonPath("$.content[0].id", is(userId)));

    verify(userService).findAll(paging);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void findUsersForPageSortAndDirTest() throws Exception {
    int passedPage = 0;

    ArrayList<User> userList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      User user = new User();
      user.setId(i);
      user.setUserName("name" + i);
      userList.add(user);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "userName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, userList.size(), sortFinal);
    Page<User> pageUser = new PageImpl<>(userList, pageRequest, userList.size());

    Pageable paging = PageRequest.of(passedPage, userList.size(), sortFinal);
    when(userService.findAll(paging)).thenReturn(pageUser);

    this.mockMvc.perform(get("/user/page?page=" + passedPage + "&size=" + userList.size() + "&sort=id&dir=asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(userList.size())))
        .andExpect(jsonPath("$.content[0].id", is(0)))
        .andExpect(jsonPath("$.content[0].userName", is("name" + 0)));

    verify(userService).findAll(paging);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void findTaxaForPageSortAndDirTest2() throws Exception {
    int passedPage = 0;

    ArrayList<User> userList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      User user = new User();
      user.setId(1-i);
      user.setUserName("name" + i);
      userList.add(user);
    }

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.DESC, "id"));
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "userName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, userList.size(), sortFinal);
    Page<User> pageUser = new PageImpl<>(userList, pageRequest, userList.size());

    Pageable paging = PageRequest.of(passedPage, userList.size(), sortFinal);
    when(userService.findAll(paging)).thenReturn(pageUser);

    this.mockMvc.perform(get("/user/page?page=" + passedPage + "&size=" + userList.size() + "&sort=id&dir=desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(userList.size())))
        .andExpect(jsonPath("$.content[0].id", is(1)))
        .andExpect(jsonPath("$.content[0].userName", is("name" + 0)));

    verify(userService).findAll(paging);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void findUsersForPageFilterTest() throws Exception {
    int passedSize = 1;
    int passedPage = 0;
    String passedFilter = "test";

    int userId = 1;

    User user = new User();
    user.setId(userId);
    ArrayList<User> userList = new ArrayList<>();
    userList.add(user);

    ArrayList<Sort.Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "userName"));
    Sort sortFinal = Sort.by(sortBy);

    PageRequest pageRequest = PageRequest.of(passedPage, passedSize, sortFinal);
    Page<User> pageUser = new PageImpl<>(userList, pageRequest, userList.size());

    Pageable paging = PageRequest.of(passedPage, passedSize, sortFinal);
    when(userService.findByUserNameContaining(passedFilter, paging)).thenReturn(pageUser);

    this.mockMvc.perform(get("/user/page?page=" + passedPage + "&size=" + passedSize + "&filter=" + passedFilter))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.content", hasSize(passedSize)))
        .andExpect(jsonPath("$.content[0].id", is(userId)));

    verify(userService).findByUserNameContaining(passedFilter, paging);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void saveUserTest() throws Exception {
    int userId = 1;

    User user = new User();
    user.setId(userId);

    when(userService.save(user)).thenReturn(user);

    this.mockMvc.perform(put("/user")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", is(userId)));

    verify(userService).save(user);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void deleteUserTest() throws Exception {
    int userId = 1;

    User user = new User();
    user.setId(userId);

    when(userService.findById(userId)).thenReturn(Optional.of(user));

    this.mockMvc.perform(delete("/user/1"))
        .andExpect(status().isOk());

    verify(userService).findById(userId);
    verify(userService).delete(user);
    verifyNoMoreInteractions(userService);
  }

  @Test
  void deleteUserNotFoundTest() throws Exception {
    int userId = 1;

    User user = new User();
    user.setId(userId);

    when(userService.findById(userId)).thenReturn(Optional.empty());

    this.mockMvc.perform(delete("/user/1"))
        .andExpect(status().isNotFound());

    verify(userService).findById(userId);
    verifyNoMoreInteractions(userService);
  }

}
