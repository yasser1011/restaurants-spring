package com.example.backend.controller;

import com.example.backend.models.Restaurant;
import com.example.backend.models.Review;
import com.example.backend.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @PostMapping(path = "/register")
    String saveRestaurant(@RequestBody Restaurant restaurant) throws Exception {
        Restaurant savedRes = restaurantService.saveRestaurant(restaurant);
        return "saved";
    }

    @GetMapping
    List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping(path = "/{restaurantId}")
    HashMap<String, Object> getRestaurant(@PathVariable("restaurantId") Long restaurantId) throws Exception {
        HashMap<String, Object> restaurantMap = restaurantService.getRestaurant(restaurantId);

        return restaurantMap;
    }

    @PostMapping(path = "/addReview")
    Review addReview(@RequestBody Review review) throws Exception {

        return restaurantService.addReview(review);
    }

    @PostMapping(path = "/edit/{restaurantId}")
    String editRestaurant(@RequestBody Restaurant restaurant,
                          @PathVariable("restaurantId") Long restaurantId) throws Exception {
        return restaurantService.editRestaurant(restaurant, restaurantId);
    }

//    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping(path = "/delete/{restaurantId}")
    String deleteRestaurant(@PathVariable("restaurantId") Long restaurantId){
        return restaurantService.deleteRestaurant(restaurantId);
    }
}
