package com.management.product.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
        SELECT p
        FROM Product p
            WHERE LOWER(p.name)
            LIKE LOWER(CONCAT('%', :name, '%'))
    """)
    Page<Product> findProductByName(@Param("name") String name, Pageable pageable);
}
