package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String location;
    private Integer priceRange;

    @Column(columnDefinition = "varchar")
    private String image;

    @Transient
    private double averageReview;

    @Transient
    private int numberOfReviews;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    public Restaurant(){}

    public Restaurant(Long id, String name, String location, Integer priceRange, String image) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.priceRange = priceRange;
        this.image = image;
    }

    public Restaurant(String name, String location, Integer priceRange, String image) {
        this.name = name;
        this.location = location;
        this.priceRange = priceRange;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(int priceRange) {
        this.priceRange = priceRange;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getAverageReviews() {
        double sum = this.reviews.stream().mapToDouble(Review::getRating).sum();
        return sum / this.reviews.size();
    }

    public void setAverageReviews(double averageReview) {
        this.averageReview = averageReview;
    }

    public int getNumberOfReviews() {
        return reviews.size();
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
