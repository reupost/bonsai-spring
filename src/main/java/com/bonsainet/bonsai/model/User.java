package com.bonsainet.bonsai.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user")
@Data
@Slf4j
public class User { //implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  String ROLE_PREFIX = "ROLE_";

  private Integer id;

  private String userName;
  private String email;
  private String bio;

  @DateTimeFormat
  private LocalDateTime dateRegistered;

  public User() {
  }

  public User(Integer id) {
    this.id = id;
  }

//  @Override
//  public Collection<? extends GrantedAuthority> getAuthorities() {
//    List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
//
//    list.add(new SimpleGrantedAuthority(ROLE_PREFIX + "ADMIN"));
//
//    return list;
//  }
//
//  public String getPassword() {
//    return null;
//  }
//
//  public String getUsername() {
//    return null;
//  }
//
//  public boolean isAccountNonExpired() {
//    return true;
//  }
//
//  public boolean isAccountNonLocked() {
//    return true;
//  }
//
//  public boolean isCredentialsNonExpired() {
//    return true;
//  }
//
//  public boolean isEnabled() {
//    return true;
//  }
}
