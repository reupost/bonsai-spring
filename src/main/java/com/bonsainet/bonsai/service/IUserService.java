package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface IUserService {

  List<User> findAll();

  Page<User> findAll(Pageable pageable);

  User save(User b);

  void delete(User b);

  Optional<User> findById(Integer id);

  Page<User> findByUserNameContaining(String name, Pageable pageable);

  Long count();

  User toUser(UserDTO userDto);

  UserDTO toDto(User user);

}
