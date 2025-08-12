package com.example.Mapping.Controller;

import com.example.Mapping.CustomException.ResourceNotFoundException;
import com.example.Mapping.RequestBean.*;
import com.example.Mapping.ResponceBean.*;
import com.example.Mapping.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductMasterController {

    private final ProductService productService;

    // Save only product
    @PostMapping("/save")
    public ResponseEntity<?> saveOnlyProduct(@Valid @RequestBody ProductRequestDTO dto) {
        try {
            ProductResponseDTO response = productService.saveOnlyProduct(dto);
            return ResponseEntity.ok(Map.of("message", "Product saved successfully", "product", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save product: " + e.getMessage()));
        }
    }

    // Save full product
    @PostMapping("/save-full")
    public ResponseEntity<?> saveFullProduct(@Valid @RequestBody ProductRequestDTO dto) {
        try {
            ProductResponseDTO response = productService.saveFullProduct(dto);
            return ResponseEntity.ok(Map.of("message", "Full product saved successfully", "product", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save full product: " + e.getMessage()));
        }
    }

    // Add rating
    @PostMapping("/{productCode}/add-rating")
    public ResponseEntity<?> addRatingToProduct(
            @PathVariable String productCode,
            @Valid @RequestBody RatingRequestDTO ratingDTO) {
        try {
            ProductResponseDTO response = productService.addRatingToProduct(productCode, ratingDTO);
            return ResponseEntity.ok(Map.of("message", "Rating added successfully", "product", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add rating: " + e.getMessage()));
        }
    }

    // Add feedbacks
    @PostMapping("/{productCode}/add-feedbacks")
    public ResponseEntity<?> addFeedbacksToProduct(
            @PathVariable String productCode,
            @Valid @RequestBody List<FeedbackRequestDTO> feedbackDTOs) {
        try {
            ProductResponseDTO response = productService.addFeedbacksToProduct(productCode, feedbackDTOs);
            return ResponseEntity.ok(Map.of("message", "Feedbacks added successfully", "product", response));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add feedbacks: " + e.getMessage()));
        }
    }

    // Add tags
    @PostMapping("/{productCode}/add-tags")
    public ResponseEntity<?> addTagsToProduct(
            @PathVariable String productCode,
            @Valid @RequestBody Set<TagRequestDTO> tagDTOs) {
        try {
            String message = productService.addTagsToProduct(productCode, tagDTOs);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add tags: " + e.getMessage()));
        }
    }

    // Get all products
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            List<ProductResponseDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(Map.of("products", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get products: " + e.getMessage()));
        }
    }

    // Paginated products
    @GetMapping("/paginated")
    public ResponseEntity<?> getPaginatedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productCode") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            Map<String, Object> response = productService.getPaginatedProducts(page, size, sortBy, direction);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get paginated products: " + e.getMessage()));
        }
    }

    // Search custom fields
    @GetMapping("/search-custom-fields")
    public ResponseEntity<?> searchCustomFields(
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String ratingCode,
            @RequestParam(required = false) String feedbackLabel,
            @RequestParam(required = false) String tagCode) {
        try {
            List<ProductSearchResponseDTO> results = productService.searchCustomFields(productCode, ratingCode, feedbackLabel, tagCode);
            return ResponseEntity.ok(Map.of("results", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to search: " + e.getMessage()));
        }
    }

    // Search products with low rating
    @GetMapping("/low-rating")
    public ResponseEntity<?> searchProductsWithLowRating(@RequestParam int overallrate) {
        try {
            List<Map<String, Object>> results = productService.searchProductsWithLowRating(overallrate);
            return ResponseEntity.ok(Map.of("results", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to search low rating products: " + e.getMessage()));
        }
    }

    // Get product by code
    @GetMapping("/{productCode}")
    public ResponseEntity<?> getByCode(@PathVariable String productCode) {
        try {
            ProductResponseDTO product = productService.getByCode(productCode);
            return ResponseEntity.ok(Map.of("product", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get product: " + e.getMessage()));
        }
    }

    // Update product
    @PutMapping("/{productCode}")
    public ResponseEntity<?> updateProduct(
            @PathVariable String productCode,
            @Valid @RequestBody ProductRequestDTO dto) {
        try {
            ProductResponseDTO updated = productService.updateProduct(productCode, dto);
            return ResponseEntity.ok(Map.of("message", "Product updated successfully", "product", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update product: " + e.getMessage()));
        }
    }

    // Delete product
    @DeleteMapping("/{productCode}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productCode) {
        try {
            productService.deleteProduct(productCode);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully: " + productCode));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete product: " + e.getMessage()));
        }
    }
}
