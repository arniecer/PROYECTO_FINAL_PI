package com.grandspicy.controller;

import com.grandspicy.service.ReviewService;
import com.grandspicy.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final UserService userService;
    private final ReviewService reviewService;

    public ProfileController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        var user = userService.findByUsername(auth.getName());
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("reviews", reviewService.findByUserId(user.get().getId()));
        }
        return "profile";
    }
}
