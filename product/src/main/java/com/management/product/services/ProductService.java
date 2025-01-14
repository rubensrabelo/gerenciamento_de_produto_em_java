package com.management.product.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.management.product.data.dto.ProductDTO;
import com.management.product.mapper.DozerMapper;
import com.management.product.models.Product;
import com.management.product.repositories.ProductRepository;
import com.management.product.services.exceptions.DatabaseException;
import com.management.product.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	private Logger logger = Logger.getLogger(ProductService.class.getName());
	
	@Autowired
	private ProductRepository repository;
	
	public List<ProductDTO> findAll() {
		logger.info("Find all products!");
		
		List<Product> products = repository.findAll();
		return DozerMapper.parseListObjects(products, ProductDTO.class);
	}
	
	public ProductDTO findById(Long id) {
		logger.info("Finding one product!");
		
		Product product = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));
		return DozerMapper.parseObject(product, ProductDTO.class);
	}
	
	public ProductDTO create(ProductDTO product) {
		logger.info("Creating one person!");
		var entity = DozerMapper.parseObject(product, Product.class);
		var vo = DozerMapper.parseObject(repository.save(entity), ProductDTO.class);
		return vo;
	}
	
	public void delete(Long id) {
		try {
			logger.info("Deleting one person!");
			
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Product", id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public ProductDTO update(Long id, ProductDTO productVOUpdated) {
		try {
			logger.info("Updating one person!");
			
			Product product = repository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Project", id));
			var productUpdated = DozerMapper.parseObject(productVOUpdated, Product.class);
			updateData(product, productUpdated);
			repository.save(product);
			var vo = DozerMapper.parseObject(product, ProductDTO.class);
			return vo;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Product", id);
		}
	}

	private void updateData(Product product, Product productUpdated) {
		product.setName(productUpdated.getName());
		product.setPrice(productUpdated.getPrice());
	}
}
