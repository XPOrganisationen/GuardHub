package com.guardhub.city;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, String> {
    Optional<City> findCityByCityName(String cityName);
    void deleteCityByCityName(String cityName);
    boolean existsByCityName(String cityName);
}
