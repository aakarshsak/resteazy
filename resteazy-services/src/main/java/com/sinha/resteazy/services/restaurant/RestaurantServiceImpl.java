package com.sinha.resteazy.services.restaurant;

import com.sinha.resteazy.daos.RestaurantRepository;
import com.sinha.resteazy.entities.Restaurant;
import com.sinha.resteazy.exceptions.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void addRestaurant(Restaurant restaurant) {
        Restaurant restaurant1 = restaurantRepository.save(restaurant);
        System.out.println(restaurant1);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        //System.out.println(optionalRestaurant.get());
        if(!optionalRestaurant.isPresent()) {
            throw new RestaurantNotFoundException("Invalid restaurant id!!!");
        }

        return optionalRestaurant.get();
    }

    @Override
    public void updateRestaurant(long id, Restaurant restaurant) {
        Restaurant restaurant1 = getRestaurantById(id);

        restaurant1.setAddress(restaurant.getAddress());
        restaurant1.setName(restaurant.getName());
        restaurant1.setPhone(restaurant.getPhone());

        restaurantRepository.save(restaurant1);
    }

    @Override
    public Restaurant deleteRestaurantById(long id) {
        Restaurant restaurant = getRestaurantById(id);
        restaurantRepository.delete(restaurant);
        return restaurant;
    }

    @Override
    public List<Restaurant> searchRestaurants(String search) {
        return restaurantRepository.findByNameContainingIgnoreCase(search);
    }
}
