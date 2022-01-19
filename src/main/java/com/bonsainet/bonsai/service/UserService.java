package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.model.UserDTO;
import com.bonsainet.bonsai.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

  private final ApplicationContext context;

  private final UserRepository repository;

  public UserService(ApplicationContext context, UserRepository repository) {
    this.context = context;
    this.repository = repository;
  }

  @Override
  public List<User> findAll() {
    return (List<User>) repository.findAll();
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    return (Page<User>) repository.findAll(pageable);
  }


  @Override
  public User save(User u) {
    if (u != null) {
      return repository.save(u);
    } else {
      throw new IllegalArgumentException("invalid user");
    }
  }

  @Override
  public void delete(User t) {
    repository.delete(t);
  }

  @Override
  public Optional<User> findById(Integer id) {
    return repository.findById(id);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return repository.findByEmail(email);
  }

  @Override
  public Page<User> findByUserNameContaining(String name, Pageable pageable) {
    return repository.findByUserNameContaining(name, pageable);
  }


  @Override
  public Long count() {
    return repository.count();
  }

  //TODO not a huge fan of this at all
  public User toUser(UserDTO userDto) {
    IUserMapperImpl iUserMapper = new IUserMapperImpl();
    User user = iUserMapper.toUser(userDto);
    return user;
  }

  public UserDTO toDto(User user) {
    IUserMapperImpl iUserMapper = new IUserMapperImpl();
    UserDTO userDTO = iUserMapper.toDTO(user);
    return userDTO;
  }
}
