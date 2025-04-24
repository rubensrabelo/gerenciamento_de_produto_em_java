package com.management.product.controllers;

import java.net.URI;
import java.util.List;

import com.management.product.controllers.docs.ProductControllerDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.management.product.data.dto.ProductDTO;
import com.management.product.services.ProductService;

@RestController
@RequestMapping(value = "products")
public class ProductController implements ProductControllerDocs {
	
	@Autowired
	private ProductService service;
	
	@GetMapping(produces = {
			MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_YAML_VALUE
	})
	@Override
	public ResponseEntity<PagedModel<EntityModel<ProductDTO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
	) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));

		return ResponseEntity.ok().body(service.findAll(pageable););
	}
	
	@GetMapping(value = "/{id}",
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_YAML_VALUE
			})
	@Override
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO product = service.findById(id);
		return ResponseEntity.ok().body(product);
	}
	
	@PostMapping(
			consumes = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_YAML_VALUE
			},
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_YAML_VALUE
			}
			)
	@Override
	public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product) {
		product = service.create(product);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(product);
	}
	
	@DeleteMapping(value = "/{id}")
	@Override
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(
			value = "/{id}",
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_YAML_VALUE
			}
	)
	public ResponseEntity<ProductDTO> productIsNotInStock(@PathVariable Long id) {
		ProductDTO product = service.productIsNotInStock(id);
		return ResponseEntity.ok().body(product);
	}
	
	@PutMapping(
			consumes = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_YAML_VALUE
			},
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_YAML_VALUE
			}
			)
	@Override
	public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO product) {
		product = service.update(product);
		return ResponseEntity.ok().body(product);
	}
}
