package com.guardhub.user;

import jakarta.persistence.*;
import org.hibernate.annotations.DiscriminatorFormula;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
/*
WHAT CAME BEFORE THE FOLLOWING LINE CAUSED ME TO WASTE THREE HOURS.
TOOK ME 3 HOURS TO FIX THIS, H2 BUILT ITS OWN WRONG CONSTRAINT, EXPECTING AN ENUM -- MAX */
@DiscriminatorFormula("case when user_type = 'ADMIN' then 'ADMIN' else 'GUARD' end")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String password; // NOTE: NEEDS HASHING

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    public User() {}

    public User(String name, String password, String email, String phoneNumber) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(Long userId, String name, String password, String email, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return userId != null && this.userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
