package com.management.product.services;

import java.util.List;
import java.util.logging.Logger;

import com.management.product.services.exceptions.RequiredObjectIsNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.management.product.controllers.ProductController;
import com.management.product.data.dto.ProductDTO;
import com.management.product.unittest.mapper.DozerMapper;
import com.management.product.models.Product;
import com.management.product.repositories.ProductRepository;
import com.management.product.services.exceptions.DatabaseException;
import com.management.product.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
	
	private Logger logger = Logger.getLogger(ProductService.class.getName());
	
	private final ProductRepository repository;
	private final PagedResourcesAssembler<ProductDTO> assembler;

	public ProductService(ProductRepository repository, PagedResourcesAssembler<ProductDTO> assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}
	
	public PagedModel<EntityModel<ProductDTO>> findAll(Pageable pageable) {
		logger.info("Find all products!");
		
		var dtoResponse = repository.findAll(pageable)
				.map(prod -> {
					var dto = DozerMapper.parseObject(prod, ProductDTO.class);
					addHateoasLinks(dto);
					return dto;
				});
		Link findAllLinks = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(ProductController.class)
						.findAll(
								pageable.getPageNumber(),
								pageable.getPageSize(),
								String.valueOf(pageable.getSort())
						)
		).withSelfRel();
		return assembler.toModel(dtoResponse, findAllLinks);
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
		if(product == null) throw new RequiredObjectIsNullException();
		logger.info("Creating one person!");
		var entity = DozerMapper.parseObject(product, Product.class);
		var dto = DozerMapper.parseObject(repository.save(entity), ProductDTO.class);
		addHateoasLinks(dto);
		return dto;
	}
	
	public void delete(Long id) {
		try {
			logger.info("Deleting one person!");
			
			Product entity = repository.findById(id)
								.orElseThrow(() -> new ResourceNotFoundException("Product", id));
			repository.delete(entity);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Product", id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public ProductDTO update(ProductDTO productDTOUpdated) {
		try {
			if(productDTOUpdated == null) throw new RequiredObjectIsNullException();

			logger.info("Updating one person!");
			
			Product entity = repository.findById(productDTOUpdated.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Product", productDTOUpdated.getId()));
			var updated = DozerMapper.parseObject(productDTOUpdated, Product.class);
			updateData(entity, updated);
			repository.save(entity);
			var dto = DozerMapper.parseObject(entity, ProductDTO.class);
			addHateoasLinks(dto);
			return dto;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Product", productDTOUpdated.getId());
		}
	}

	@Transactional
	public ProductDTO productIsNotInStock(Long id) {
		logger.info("Informing that the product is not in stock");

		repository.findById((id))
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));

		repository.productIsNotInStock(id);

		Product entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));
		var dto = DozerMapper.parseObject(entity, ProductDTO.class);
		addHateoasLinks(dto);
		return dto;
	}

	private void updateData(Product entity, Product updated) {
		entity.setName(updated.getName());
		entity.setPrice(updated.getPrice());
		entity.setInStock(updated.getInStock());
	}
	
	private void addHateoasLinks(ProductDTO dto) {
		 dto.add(linkTo(methodOn(ProductController.class).findById(dto.getId())).withSelfRel().withType("GET"));
	        dto.add(linkTo(methodOn(ProductController.class).findAll(0, 10, "desc")).withRel("findAll").withType("GET"));
	        dto.add(linkTo(methodOn(ProductController.class).create(dto)).withRel("create").withType("POST"));
	        dto.add(linkTo(methodOn(ProductController.class).update(dto)).withRel("update").withType("PUT"));
	        dto.add(linkTo(methodOn(ProductController.class).productIsNotInStock(dto.getId())).withRel("productIsNotInStock").withType("PATCH"));
	        dto.add(linkTo(methodOn(ProductController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
	}
}
