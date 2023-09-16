package com.sinha.resteazy.services.restaurant;

import com.sinha.resteazy.entities.Restaurant;

import java.util.List;

public interface RestaurantService {
    public void addRestaurant(Restaurant restaurant);
    public List<Restaurant> getAllRestaurants();
    Restaurant getRestaurantById(long id);
    void updateRestaurant(long id, Restaurant restaurant);
    Restaurant deleteRestaurantById(long id);
    List<Restaurant> searchRestaurants(String search);
}
