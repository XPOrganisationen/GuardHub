package com.guardhub.repository;

import com.guardhub.model.User;
import com.guardhub.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
Optional<User> findUserByEmail(String email);

List<User> findAllByNameContainingIgnoreCase(String name);

List<User> findAllByUserType(UserType userType);


}
