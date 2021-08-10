package com.example.backend.repository;

import com.example.backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    public List<Review> findByRestaurantId(Long id);

    @Modifying
    @Query("delete from Review r where r.restaurantId = ?1")
    public void deleteReviewsByRestaurantId(Long restaurantId);
}
