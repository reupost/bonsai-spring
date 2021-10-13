package com.bonsainet.bonsai.service;

import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper
public interface IUserMapper {
  //TODO is there a way of using the @Mapping to convert instead of doing it manually?
  UserDTO toDTO(User user);

  User toUser(UserDTO userDto);
}

