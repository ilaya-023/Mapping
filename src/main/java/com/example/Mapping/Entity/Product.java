package com.example.Mapping.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_product_code", columnList = "product_code"))
public class Product {

    @Id
    @GeneratedValue
    private UUID product_id;

    @Column(name = "product_code", unique = true)
    private String productCode;
    @Column(name="product_name")
    String productName;
    private String product_brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Feedback> feedbacks = new ArrayList<>();
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Rating rating;

    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o)|| getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return product_id != null && product_id.equals(product.product_id);
    }

    @Override
    public int hashCode() {
        return product_id != null ? product_id.hashCode() : 0;
    }


}
