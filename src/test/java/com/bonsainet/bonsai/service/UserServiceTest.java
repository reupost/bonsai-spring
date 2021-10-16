package com.bonsainet.bonsai.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.repository.UserRepository;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

class UserServiceTest {

  private UserService userService;
  private ApplicationContext applicationContext;

  UserRepository userRepository;

  @BeforeEach
  void setup() {
    applicationContext = mock(ApplicationContext.class);
    userRepository = mock(UserRepository.class);
    userService = new UserService(applicationContext, userRepository);
  }

  @Test
  void shouldThrowExceptionWhenSaveUserIsNull() {
    assertThrows(IllegalArgumentException.class, () -> userService.save(null));
  }

  @Test
  void shouldSaveUser() {
    User user = new User();
    user.setId(1);

    userService.save(user);

    verify(userRepository).save(user);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldFindAllUsers() {
    userService.findAll();

    verify(userRepository).findAll();
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldFindAllUsersPageable() {
    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);

    userService.findAll(paging);

    verify(userRepository).findAll(paging);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldDeleteUser() {
    User user = new User();
    user.setId(1);

    userService.delete(user);

    verify(userRepository).delete(user);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldFindByIdUser() {
    userService.findById(1);

    verify(userRepository).findById(1);
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldCountUser() {
    userService.count();

    verify(userRepository).count();
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  void shouldFindByUserNameContaining() {
    ArrayList<Order> sortBy = new ArrayList<>();
    sortBy.add(new Sort.Order(Sort.Direction.ASC, "id"));
    Sort sortFinal = Sort.by(sortBy);

    Pageable paging = PageRequest.of(0, 1, sortFinal);

    userService.findByUserNameContaining("test", paging);

    verify(userRepository).findByUserNameContaining("test", paging);
    verifyNoMoreInteractions(userRepository);
  }
}
