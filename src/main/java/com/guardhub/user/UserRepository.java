package com.guardhub.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    List<User> findAllByNameContainingIgnoreCase(String name);

    @Query("SELECT admin FROM Admin admin")
    List<Admin> findAllAdmins();

    @Query("SELECT guard FROM Guard guard")
    List<Guard> findAllGuards();
}
