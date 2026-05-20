package com.grandspicy.controller;

import com.grandspicy.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {

    private final ProductService productService;

    public ImageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/images/product/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        var product = productService.findById(id);
        if (product.isPresent()) {
            byte[] imageData = product.get().getImageData();
            if (imageData != null && imageData.length > 0) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imageData);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
