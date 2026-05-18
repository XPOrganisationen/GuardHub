package com.guardhub;

import com.guardhub.user.Admin;
import com.guardhub.user.Guard;
import com.guardhub.user.User;
import com.guardhub.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllAdminsReturnsOnlyAdmins() {
        Admin admin = new Admin("Admin Adminsen", "password123", "admin@test.dk", "12345678");
        Guard guard = new Guard("Guard Guardsen", "password123", "guard@test.dk", "87654321");

        userRepository.save(admin);
        userRepository.save(guard);

        List<Admin> admins = userRepository.findAllAdmins();

        Assertions.assertEquals(1, admins.size());
        Assertions.assertEquals("Admin Adminsen", admins.getFirst().getName());
    }

    @Test
    public void findAllGuardsReturnsOnlyGuards() {
        Admin admin = new Admin("Admin Adminsen", "password123", "admin2@test.dk", "12345678");
        Guard guard = new Guard("Guard Guardsen", "password123", "guard2@test.dk", "87654321");

        userRepository.save(admin);
        userRepository.save(guard);

        List<Guard> guards = userRepository.findAllGuards();

        Assertions.assertEquals(1, guards.size());
        Assertions.assertEquals("Guard Guardsen", guards.getFirst().getName());
    }

    @Test
    public void findUserByEmailReturnsCorrectUser() {
        Guard guard = new Guard("Guard Guardsen", "password123", "guard3@test.dk", "11223344");

        userRepository.save(guard);

        Optional<User> foundUser = userRepository.findUserByEmail("guard3@test.dk");

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals("Guard Guardsen", foundUser.get().getName());
    }

    @Test
    public void findUserByEmailReturnsEmptyWhenEmailDoesNotExist() {
        Optional<User> foundUser = userRepository.findUserByEmail("missing@test.dk");

        Assertions.assertTrue(foundUser.isEmpty());
    }

    @Test
    public void findAllByNameContainingIgnoreCaseFindsMatchingUsers() {
        Guard guard1 = new Guard("Guard Guardsen", "password123", "guard4@test.dk", "11111111");
        Guard guard2 = new Guard("Guard Guardsen", "password123", "guard5@test.dk", "22222222");
        Admin admin = new Admin("Admin Adminsen", "password123", "admin3@test.dk", "33333333");

        userRepository.saveAll(List.of(guard1, guard2, admin));

        List<User> result = userRepository.findAllByNameContainingIgnoreCase("guard");

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().allMatch(user -> user.getName().equals("Guard Guardsen")));
    }
}