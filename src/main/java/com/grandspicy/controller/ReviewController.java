package com.grandspicy.controller;

import com.grandspicy.model.Review;
import com.grandspicy.service.ProductService;
import com.grandspicy.service.ReviewService;
import com.grandspicy.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    public ReviewController(ReviewService reviewService, UserService userService, ProductService productService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/add")
    public String addReview(@RequestParam Long productId,
                            @RequestParam int rating,
                            @RequestParam String comment,
                            Authentication auth) {
        var user = userService.findByUsername(auth.getName());
        var product = productService.findById(productId);
        if (user.isPresent() && product.isPresent()) {
            Review review = new Review(user.get(), product.get(), rating, comment);
            reviewService.save(review);
        }
        return "redirect:/products/" + productId;
    }
}
