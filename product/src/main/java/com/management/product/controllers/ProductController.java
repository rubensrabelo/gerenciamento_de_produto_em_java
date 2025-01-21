package com.management.product.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.management.product.data.dto.ProductDTO;
import com.management.product.services.ProductService;

@RestController
@RequestMapping(value = "products")
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@GetMapping(produces = {
			MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_YAML_VALUE
	})
	private ResponseEntity<List<ProductDTO>> findAll() {
		List<ProductDTO> products = service.findAll();
		return ResponseEntity.ok().body(products);
	}
	
	@GetMapping(value = "/{id}",
			produces = {
					MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_YAML_VALUE
			})
	private ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
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
	private ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product) {
		product = service.create(product);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(product);
	}
	
	@DeleteMapping(value = "/{id}")
	private ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}",
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
	private ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO product) {
		product = service.update(id, product);
		return ResponseEntity.ok().body(product);
	}
}
