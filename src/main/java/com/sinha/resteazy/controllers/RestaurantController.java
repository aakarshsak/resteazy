package com.sinha.resteazy.controllers;

import com.sinha.resteazy.entities.SuccessResponse;
import com.sinha.resteazy.entities.User;
import com.sinha.resteazy.services.restaurant.RestaurantService;
import com.sinha.resteazy.entities.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("")
    public List<Restaurant> getRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @PostMapping("")
    public ResponseEntity<SuccessResponse> addRestaurant(@RequestBody Restaurant restaurant) {
        restaurantService.addRestaurant(restaurant);
        return new ResponseEntity<>(new SuccessResponse(restaurant, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public void updateRestaurant(@PathVariable long id, @RequestBody Restaurant restaurant) {
        restaurantService.updateRestaurant(id, restaurant);
    }

    @GetMapping("/{id}")
    public Restaurant getSingleRestaurant(@PathVariable long id) {
        return restaurantService.getRestaurantById(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponse> deleteRestaurant(@PathVariable long id) {
        Restaurant restaurant = restaurantService.deleteRestaurantById(id);
        return new ResponseEntity<>(new SuccessResponse(restaurant, HttpStatus.ACCEPTED.value()), HttpStatus.ACCEPTED);
    }
}
