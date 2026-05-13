package com.guardhub.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllByName(String name) {
        return userRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Admin> findAllAdmins() {
        return userRepository.findAllAdmins();
    }

    @Override
    public List<Guard> findAllGuards() {
        return userRepository.findAllGuards();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No user found with id: " + id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));
    }

    @Override
    public Admin addAdmin(Admin admin) {
        if (userRepository.findUserByEmail(admin.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + admin.getEmail());
        }
        // TODO: Hash password before saving. Spring Security in pom.xml and a password encoder (bean) needed. I may need help with the password-encoder.
        return userRepository.save(admin);
    }

    @Override
    public Guard addGuard(Guard guard) {
        if (userRepository.findUserByEmail(guard.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + guard.getEmail());
        }
        // TODO: Hash password before saving. Spring Security in pom.xml and a password encoder (bean) needed. I may need help with the password-encoder.
        return userRepository.save(guard);
    }

    @Override
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("No user found with id: " + user.getUserId());
        }

        // TODO: Handle password update in seperate method to avoid accidental null password issues etc.
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("No user found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Wrong email or password"));

        // TODO: Password check with crypt needed to be implemented
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong email or password");
        }

        return user;
    }
}
