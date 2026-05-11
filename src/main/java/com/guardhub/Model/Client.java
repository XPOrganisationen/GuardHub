package com.guardhub.Model;

import com.guardhub.Enum.City;
import jakarta.persistence.*;


@Entity
@Table(name="clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    private String clientName;
    private String clientEmail;
    private String clientPhoneNumber;

    @Enumerated(EnumType.STRING)
    private City city;

    private String clientAddress;

    public Client() {
    }

    public Client(String clientName, String clientEmail, String clientPhoneNumber, City city, String clientAddress) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhoneNumber = clientPhoneNumber;
        this.city = city;
        this.clientAddress = clientAddress;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }


}
