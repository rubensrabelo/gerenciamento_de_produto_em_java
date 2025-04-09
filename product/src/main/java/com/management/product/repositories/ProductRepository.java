package com.management.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.product.models.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
        UPDATE Product p
        SET p.isInStock = false
        WHERE p.id = :id

    """)
    void productIsNotInStock(@Param("id") Long id);
}
