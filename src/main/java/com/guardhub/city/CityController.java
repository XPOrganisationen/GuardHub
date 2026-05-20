package com.guardhub.city;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> getAllCities() {
        return cityService.findAllCities();
    }

    @PostMapping
    public City addCity(@RequestBody City city) {
        return cityService.addCity(city);
    }

    @PutMapping
    public City updateCity(@RequestBody String cityName, @RequestBody City city) {
        return cityService.updateCity(cityName, city);
    }

    @DeleteMapping("/{cityName}")
    public void RemoveCityByCityName(@PathVariable String cityName) {
        cityService.removeCityByCityName(cityName);
    }
}