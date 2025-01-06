package com.management.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.product.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
