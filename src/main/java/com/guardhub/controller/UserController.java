package com.guardhub.controller;

import com.guardhub.model.DTOs.UserCredentials;
import com.guardhub.model.User;
import com.guardhub.model.UserType;
import com.guardhub.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/by-name/{name}")
    public List<User> getUsersByName(@PathVariable String name) {
        return userService.findAllByName(name);
    }

    @GetMapping("/by-type/{userType}")
    public List<User> getUsersByType(@PathVariable UserType userType){
        return userService.findAllByUserType(userType);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public User login(@RequestBody UserCredentials credentials)
    {
        return userService.login(credentials.email(), credentials.password());
    }
    // TODO: Need to make /addUser /deleteUser and /getAllUsers an admin-only
    // TODO: After logging in the User needs to be redirected depending on admin/guard
    // TODO: Need to make a DTO without a password so it doesn't send it back to frontend. (Need help with Crypt).(Maybe a UserResponse DTO with everything but password so the login returns that instead?)

}
