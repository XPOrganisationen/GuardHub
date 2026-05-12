package com.guardhub.city;

import com.guardhub.exceptions.EntityAlreadyExistsException;
import com.guardhub.exceptions.EntityDoesNotExistException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;


    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City findCityByCityName(String cityName) {
        return cityRepository.findCityByCityName(cityName).orElseThrow(() -> new EntityDoesNotExistException("No city with name \"" + cityName + "\" exists"));
    }

    @Override
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public City addCity(City city) {
        if (cityRepository.existsByCityName(city.getCityName())) {
            throw new EntityAlreadyExistsException("A city with name \"" + city.getCityName() + "\" already exists");
        }

        return cityRepository.save(city);
    }

    @Override
    public City updateCity(String cityName, City city) {
        if (!cityRepository.existsByCityName(cityName)) {
            throw new EntityDoesNotExistException("No city with name \"" + cityName + "\" exists");
        }

        return cityRepository.save(city);
    }

    @Override
    public void removeCityByCityName(String cityName) {
        if (!cityRepository.existsByCityName(cityName)) {
            throw new EntityDoesNotExistException("No city with name \"" + cityName + "\" exists");
        }

        cityRepository.deleteCityByCityName(cityName);
    }
}
