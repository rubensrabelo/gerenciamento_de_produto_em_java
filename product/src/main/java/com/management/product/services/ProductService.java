package com.management.product.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.management.product.controllers.ProductController;
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
		
		List<Product> entities = repository.findAll();
		var dtos = DozerMapper.parseListObjects(entities, ProductDTO.class);
		dtos.forEach(this::addHateoasLinks);
		return dtos;
	}
	
	public ProductDTO findById(Long id) {
		logger.info("Finding one product!");
		
		Product entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));
		var dto = DozerMapper.parseObject(entity, ProductDTO.class);
		addHateoasLinks(dto);
		return dto;
	}
	
	public ProductDTO create(ProductDTO product) {
		logger.info("Creating one person!");
		var entity = DozerMapper.parseObject(product, Product.class);
		var dto = DozerMapper.parseObject(repository.save(entity), ProductDTO.class);
		addHateoasLinks(dto);
		return dto;
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
			
			Product entity = repository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Project", id));
			var updated = DozerMapper.parseObject(productVOUpdated, Product.class);
			updateData(entity, updated);
			repository.save(entity);
			var dto = DozerMapper.parseObject(entity, ProductDTO.class);
			addHateoasLinks(dto);
			return dto;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Product", id);
		}
	}

	private void updateData(Product entity, Product updated) {
		entity.setName(updated.getName());
		entity.setPrice(updated.getPrice());
	}
	
	private void addHateoasLinks(ProductDTO dto) {
		 dto.add(linkTo(methodOn(ProductController.class).findById(dto.getId())).withSelfRel().withType("GET"));
	        dto.add(linkTo(methodOn(ProductController.class).findAll()).withRel("findAll").withType("GET"));
	        dto.add(linkTo(methodOn(ProductController.class).create(dto)).withRel("create").withType("POST"));
	        dto.add(linkTo(methodOn(ProductController.class).update(dto.getId(), dto)).withRel("update").withType("PUT"));
	        dto.add(linkTo(methodOn(ProductController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
	}
}
