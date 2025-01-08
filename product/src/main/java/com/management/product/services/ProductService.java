package com.management.product.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.management.product.models.Product;
import com.management.product.repositories.ProductRepository;
import com.management.product.services.exceptions.DatabaseException;
import com.management.product.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));
		return product;
	}
	
	public Product create(Product product) {
		product = repository.save(product);
		return product;
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Product", id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public Product update(Long id, Product productUpdated) {
		try {
			Product product = repository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Project", id));
			updateData(product, productUpdated);
			repository.save(product);
			return product;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Product", id);
		}
	}

	private void updateData(Product product, Product productUpdated) {
		product.setName(productUpdated.getName());
		product.setPrice(productUpdated.getPrice());
	}
}
