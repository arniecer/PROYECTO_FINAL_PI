package com.grandspicy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    private String image;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column(nullable = false)
    private String category;

    @Column(name = "scoville_level")
    private Integer scovilleLevel;

    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    @Column(name = "purchase_link")
    private String purchaseLink;

    @Column(nullable = false)
    private Double rating;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Product() {
        this.rating = 0.0;
    }

    public Product(String name, String description, Double price, String image, String category,
                   Integer scovilleLevel, String countryOfOrigin, String purchaseLink, Double rating) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.scovilleLevel = scovilleLevel;
        this.countryOfOrigin = countryOfOrigin;
        this.purchaseLink = purchaseLink;
        this.rating = (rating != null) ? rating : 0.0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getScovilleLevel() { return scovilleLevel; }
    public void setScovilleLevel(Integer scovilleLevel) { this.scovilleLevel = scovilleLevel; }
    public String getCountryOfOrigin() { return countryOfOrigin; }
    public void setCountryOfOrigin(String countryOfOrigin) { this.countryOfOrigin = countryOfOrigin; }
    public String getPurchaseLink() { return purchaseLink; }
    public void setPurchaseLink(String purchaseLink) { this.purchaseLink = purchaseLink; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
