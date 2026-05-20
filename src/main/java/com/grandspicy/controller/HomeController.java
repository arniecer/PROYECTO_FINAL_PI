package com.grandspicy.controller;

import com.grandspicy.service.ProductService;
import com.grandspicy.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;
    private final ReviewService reviewService;

    public HomeController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("recentReviews", reviewService.findRecent(6));
        return "index";
    }
}
