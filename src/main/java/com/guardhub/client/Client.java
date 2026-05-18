package com.guardhub.client;

import com.guardhub.city.City;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    private Long id;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "\\d{3,15}")
    private String phoneNumber;

    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "city")
    private City city;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String address;

    public Client() {}

    public Client(String address, City city, String email, Long id, String name, String phoneNumber) {
        this.address = address;
        this.city = city;
        this.email = email;
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(@Nonnull String address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(@Nonnull City city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@Nonnull String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}