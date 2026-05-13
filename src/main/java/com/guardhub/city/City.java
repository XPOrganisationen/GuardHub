package com.guardhub.city;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cities")
public class City {
    @Id
    @Column(nullable = false, length = 128, name = "city_name")
    private String cityName;

    public City() {}

    public City(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityTitle) {
        this.cityName = cityTitle;
    }
}