package com.guardhub.user;

import java.util.List;

public interface UserService {
    List<User> findAll();

    List<User> findAllByName(String name);

    List<Admin> findAllAdmins();

    List<Guard> findAllGuards();

    User findById(Long id);

    User findByEmail(String email);

    Admin addAdmin(Admin admin);

    Guard addGuard(Guard guard);

    User updateUser(User user);

    void deleteUser(Long id);

    User login(String email, String password);
}
