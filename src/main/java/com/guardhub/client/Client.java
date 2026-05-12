package com.guardhub.client;

import com.guardhub.city.City;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", nullable = false)
    private Long id;

    private String name;
    private String email;

    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "city")
    private City city;

    private String address;

    public Client() {}

    public Client(String address, City city, String email, Long id, String name) {
        this.address = address;
        this.city = city;
        this.email = email;
        this.id = id;
        this.name = name;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(name, client.name) && Objects.equals(email, client.email) && Objects.equals(city, client.city) && Objects.equals(address, client.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, city, address);
    }
}