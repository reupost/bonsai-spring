package com.bonsainet.bonsai.repository;

import com.bonsainet.bonsai.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

  Page<User> findByUserNameContaining(String name, Pageable pageable);

  Optional<User> findByEmail(String email);
}
