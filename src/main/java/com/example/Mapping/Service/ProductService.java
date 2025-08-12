package com.example.Mapping.Service;

import com.example.Mapping.CustomException.ResourceNotFoundException;
import com.example.Mapping.Entity.*;
import com.example.Mapping.Mapper.*;
import com.example.Mapping.Repository.ProductRepository;
import com.example.Mapping.Repository.TagRepository;
import com.example.Mapping.RequestBean.*;
import com.example.Mapping.ResponceBean.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final TagRepository tagRepo;

    @PersistenceContext
    private final EntityManager entityManager;

    // Save only product
    public ProductResponseDTO saveOnlyProduct(ProductRequestDTO dto) {
        Product product = ProductMapper.toEntity(dto);
        Product saved = productRepo.save(product);
        return ProductMapper.toDTO(saved);
    }


    // Add rating to product
    public ProductResponseDTO addRatingToProduct(String code, RatingRequestDTO ratingDTO) {
        Product product = getProductByCode(code);
        Rating rating = RatingMapper.toEntity(ratingDTO, product);
        product.setRating(rating);
        return ProductMapper.toDTO(productRepo.save(product));
    }

    // Add feedbacks to product
    public ProductResponseDTO addFeedbacksToProduct(String code, List<FeedbackRequestDTO> feedbacks) {
        Product product = getProductByCode(code);
        if (Objects.isNull(product.getFeedbacks())) {
            product.setFeedbacks(new ArrayList<>());
        }
        feedbacks.stream()
                .map(f -> FeedbackMapper.toEntity(f, product))
                .forEach(product.getFeedbacks()::add);

        return ProductMapper.toDTO(productRepo.save(product));
    }

    // Add tags to product
    @Transactional
    public String addTagsToProduct(String productCode, Set<TagRequestDTO> tagDTOs) {
        Product product = getProductByCode(productCode);
        if (Objects.isNull(product.getTags())) product.setTags(new HashSet<>());

        for (TagRequestDTO dto : tagDTOs) {
            Tag tag = tagRepo.findByTagCode(dto.getTagCode());
            if (Objects.isNull(tag)) tag = tagRepo.save(TagMapper.toEntity(dto));
            product.getTags().add(tag);
        }

        productRepo.save(product);
        return "Tags added successfully to product: " + productCode;
    }

    // Save full product with relations
    public ProductResponseDTO saveFullProduct(ProductRequestDTO dto) {
        Product product = ProductMapper.toEntity(dto);
        return ProductMapper.toDTO(productRepo.save(product));
    }

    // Get all products
    public List<ProductResponseDTO> getAllProducts() {
        return productRepo.findAll().stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    // Paginated products
    public Map<String, Object> getPaginatedProducts(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = (direction != null && direction.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Page<Product> productPage = productRepo.findAll(
                PageRequest.of(page, size, Sort.by(sortDirection, sortBy))
        );

        return Map.of(
                "products", productPage.map(ProductMapper::toDTO).toList(),
                "currentPage", productPage.getNumber(),
                "totalItems", productPage.getTotalElements(),
                "totalPages", productPage.getTotalPages()
        );
    }

    // Search custom fields
    public List<ProductSearchResponseDTO> searchCustomFields(
            String productCode,
            String ratingCode,
            String feedbackLabel,
            String tagCode
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductSearchResponseDTO> cq = cb.createQuery(ProductSearchResponseDTO.class);
        Root<Product> productRoot = cq.from(Product.class);

        Join<Product, Rating> ratingJoin = productRoot.join("rating", JoinType.LEFT);
        Join<Product, Feedback> feedbackJoin = productRoot.join("feedbacks", JoinType.LEFT);
        Join<Product, Tag> tagJoin = productRoot.join("tags", JoinType.LEFT);

        cq.select(cb.construct(
                ProductSearchResponseDTO.class,
                productRoot.get("productCode"),
                ratingJoin.get("ratingCode"),
                feedbackJoin.get("feedback_label"),
                tagJoin.get("tagCode")
        ));

        List<Predicate> predicates = new ArrayList<>();
        if (productCode != null && !productCode.trim().isEmpty()) {
            predicates.add(cb.equal(productRoot.get("productCode"), productCode));
        }
        if (ratingCode != null && !ratingCode.trim().isEmpty()) {
            predicates.add(cb.equal(ratingJoin.get("ratingCode"), ratingCode));
        }
        if (feedbackLabel != null && !feedbackLabel.trim().isEmpty()) {
            predicates.add(cb.equal(feedbackJoin.get("feedback_label"), feedbackLabel));
        }
        if (tagCode != null && !tagCode.trim().isEmpty()) {
            predicates.add(cb.equal(tagJoin.get("tagCode"), tagCode));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(cq).getResultList();
    }

    // Low rating search
    public List<Map<String, Object>> searchProductsWithLowRating(int ratingThreshold) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Product> productRoot = cq.from(Product.class);

        Join<Product, Rating> ratingJoin = productRoot.join("rating", JoinType.LEFT);

        cq.multiselect(
                productRoot.get("productCode"),
                ratingJoin.get("ratingCode"),
                ratingJoin.get("overall_rating")
        );

        cq.where(cb.lessThan(ratingJoin.get("overall_rating"), ratingThreshold));

        List<Object[]> results = entityManager.createQuery(cq).getResultList();

        return results.stream()
                .map(row -> Map.of(
                        "productCode", row[0],
                        "ratingCode", row[1],
                        "overallRating", row[2]
                ))
                .toList();
    }

    // Get product by code
    public ProductResponseDTO getByCode(String code) {
        return ProductMapper.toDTO(getProductByCode(code));
    }

    // Update product
    public ProductResponseDTO updateProduct(String code, ProductRequestDTO dto) {
        Product product = getProductByCode(code);

        if (dto.getProductCode() != null)
            product.setProductCode(dto.getProductCode());
        if (dto.getProductName() != null)
            product.setProductName(dto.getProductName());
        if (dto.getProductBrand() != null)
            product.setProduct_brand(dto.getProductBrand());

        // Rating update
        if (dto.getRating() != null) {
            RatingRequestDTO inputRating = dto.getRating();
            Rating existingRating = product.getRating();
            if (existingRating != null && inputRating.getRatingCode() != null &&
                    inputRating.getRatingCode().equals(existingRating.getRatingCode())) {
                existingRating.setOverall_rating(inputRating.getOverallRating());
            }
        }

        // Feedback update
        if (dto.getFeedbacks() != null && product.getFeedbacks() != null) {
            for (Feedback existing : product.getFeedbacks()) {
                dto.getFeedbacks().stream()
                        .filter(f -> f.getFeedbackLabel().equals(existing.getFeedback_label()))
                        .findFirst()
                        .ifPresent(input -> {
                            if (input.getFeedbackContent() != null)
                                existing.setFeedback_content(input.getFeedbackContent());
                        });
            }
        }

        // Tag update
        if (dto.getTags() != null && product.getTags() != null) {
            for (Tag existing : product.getTags()) {
                dto.getTags().stream()
                        .filter(t -> t.getTagCode().equals(existing.getTagCode()))
                        .findFirst()
                        .ifPresent(input -> {
                            if (input.getTagName() != null)
                                existing.setTag_name(input.getTagName());
                        });
            }
        }

        return ProductMapper.toDTO(productRepo.save(product));
    }

    // Delete product
    public void deleteProduct(String code) {
        productRepo.delete(getProductByCode(code));
    }

    // Private helper
    private Product getProductByCode(String code) {
        Product product = productRepo.findByProductCode(code);
        if (product == null) {
            throw new ResourceNotFoundException("Product with code '" + code + "' not found.");
        }
        return product;
    }

}
