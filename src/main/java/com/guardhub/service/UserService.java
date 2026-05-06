package com.guardhub.service;

import com.guardhub.model.User;
import com.guardhub.model.UserType;

import java.util.List;

public interface UserService {
    List<User> findAll();

    List<User> findAllByName(String name);

    List<User> findAllByUserType(UserType userType);

    User findById(Long id);

    User findByEmail(String email);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    User login(String email, String password);

    boolean isAdmin(User user);

    boolean isGuard(User user);
}
