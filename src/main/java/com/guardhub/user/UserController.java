package com.guardhub.user;

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

    @GetMapping("/admins")
    public List<Admin> getAllAdmins() {
        return userService.findAllAdmins();
    }

    @GetMapping("/guards")
    public List<Guard> getAllGuards() {
        return userService.findAllGuards();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/by-name/{name}")
    public List<User> getUsersByName(@PathVariable String name) {
        return userService.findAllByName(name);
    }

    @PostMapping("/admin")
    public Admin addAdmin(@RequestBody Admin admin) {
        return userService.addAdmin(admin);
    }

    @PostMapping("/guard")
    public Guard addGuard(@RequestBody Guard guard) {
        return userService.addGuard(guard);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public User login(@RequestBody UserCredentials credentials) {
        return userService.login(credentials.email(), credentials.password());
    }
    // TODO: Need to make /addUser /deleteUser and /getAllUsers an admin-only
    // TODO: After logging in the User needs to be redirected depending on admin/guard
    // TODO: Need to make a DTO without a password so it doesn't send it back to frontend. (Need help with Crypt).(Maybe a UserResponse DTO with everything but password so the login returns that instead?)


    // Et eksempel på hvordan nogle endpoints kan kræve at brugeren er en Admin
    @GetMapping("/admin-only")
    public String exampleAdminOnlyEndpoint(@RequestParam Long userId) {
        User user = userService.findById(userId);
        if (!(user instanceof Admin)) throw new IllegalArgumentException("Access denied: Admin privileges required");

        return "Admin access granted!";
    }
}
