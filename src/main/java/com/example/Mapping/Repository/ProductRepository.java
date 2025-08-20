package com.example.Mapping.Repository;

//import com.example.Mapping.DTO.ProductDTO;
import com.example.Mapping.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product findByProductCode(String product_code);

}
