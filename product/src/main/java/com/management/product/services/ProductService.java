package com.management.product.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.product.models.Product;
import com.management.product.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	public List<Product> findAll() {
		List<Product> products = repository.findAll();
		return products;
	}
	
	public Product findById(Long id) {
		Product product = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Error"));
		return product;
	}
	
	public Product create() {
		return null;
	}
	
	public void delete() {
	}
	
	public Product update() {
		return null;
	}
}
