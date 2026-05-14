package com.guardhub.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    public Admin() {
        super();
        setUserType("ADMIN");
    }

    public Admin(String name, String password, String email, String phoneNumber) {
        super(name, password, email, phoneNumber);
        setUserType("ADMIN");
    }

    public Admin(Long id, String name, String password, String email, String phoneNumber) {
        super(id, name, password, email, phoneNumber);
        setUserType("ADMIN");
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                '}';
    }
}
