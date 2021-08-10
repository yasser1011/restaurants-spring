package com.example.backend.repository;

import com.example.backend.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r from Restaurant r JOIN FETCH r.reviews where r.id = ?1")
    public Restaurant findRestaurantByIdWithReviews(Long id);
}
