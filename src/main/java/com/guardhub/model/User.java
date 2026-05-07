package com.guardhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password; // NOTE: NEEDS HASHING

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    // TODO: @OneToMany relation with ShiftRegistration when we have the model.
    // private List<ShiftRegistration> registrations;

    public User(){}

    public User(Long id, String name, String password, String email, String phoneNumber, UserType userType) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }

    public boolean isAdmin() {
        return userType == UserType.ADMIN;
    }

    public boolean isGuard(){
        return userType == UserType.GUARD;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long userId) {
        this.id = userId;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" + "id=[" + id + "], " +
                "name=[" + name + '\'' + "], " +
                "email=[" + email + '\'' + "], " +
                "userType=[" + userType + "]" +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
