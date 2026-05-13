package com.guardhub.city;

import java.util.List;

public interface CityService {
    public City findCityByCityName(String cityName);
    public List<City> findAllCities();
    public City addCity(City city);
    public City updateCity(String cityName, City city);
    public void removeCityByCityName(String cityName);
}
