package com.example.backend.service;

import com.example.backend.models.Restaurant;
import com.example.backend.models.Review;
import com.example.backend.models.User;
import com.example.backend.repository.RestaurantRepository;
import com.example.backend.repository.ReviewRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository, UserRepository userRepository){
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.findAll();
    }

    public Restaurant saveRestaurant(Restaurant restaurant) throws Exception {
        if(restaurant.getName() == null || restaurant.getLocation() == null || restaurant.getPriceRange() == null || restaurant.getImage() == null)
            throw new Exception("enter all fields");
        if(restaurant.getPriceRange() > 5 || restaurant.getPriceRange() < 0)
            throw new Exception("price out of range");

        return restaurantRepository.save(restaurant);
    }

    public HashMap<String, Object> getRestaurant(Long id){
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new IllegalStateException("restaurant not found"));
        List<Review> restaurantReviews = reviewRepository.findByRestaurantId(id);
        HashMap<String, Object> restaurantMap = new HashMap<>();
        restaurantMap.put("restaurant", restaurant);
        restaurantMap.put("reviews", restaurantReviews);
        return restaurantMap;
//        return restaurantRepository.findRestaurantByIdWithReviews(id);
    }

    public Review addReview(Review review) throws Exception {
        if(review.getReview() == null || review.getRating() == null || review.getUserId() == null || review.getRestaurantId() == null)
            throw new Exception("enter all fields");
        if(review.getRating() < 0 || review.getRating() > 5)
            throw new Exception("rating out of range");

        User user = userRepository.findById(review.getUserId()).orElseThrow(() -> new IllegalStateException("user not found with that id"));
        Restaurant restaurant = restaurantRepository.findById(review.getRestaurantId()).orElseThrow(() -> new IllegalStateException("restaurant not found with that id"));

        review.setUser(user);

        Review savedReview = reviewRepository.save(review);
        return savedReview;
    }

    public String editRestaurant(Restaurant restaurant, Long restaurantId) throws Exception {
        if(restaurant.getName() == null || restaurant.getLocation() == null || restaurant.getPriceRange() == null || restaurant.getImage() == null)
            throw new Exception("enter all fields");
        Restaurant savedRestaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalStateException("restaurant not found"));

        savedRestaurant.setName(restaurant.getName());
        savedRestaurant.setLocation(restaurant.getLocation());
        savedRestaurant.setPriceRange(restaurant.getPriceRange());
        savedRestaurant.setImage(restaurant.getImage());

        return "edited";
    }

    public String deleteRestaurant(Long restaurantId){
        Restaurant savedRestaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalStateException("restaurant not found"));
        reviewRepository.deleteReviewsByRestaurantId(restaurantId);
        restaurantRepository.deleteById(restaurantId);
        return "deleted";
    }

}
