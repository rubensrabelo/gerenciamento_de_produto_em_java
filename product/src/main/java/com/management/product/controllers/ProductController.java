package com.management.product.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.management.product.data.vo.v1.ProductVO;
import com.management.product.services.ProductService;

@RestController
@RequestMapping(value = "products")
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@GetMapping
	private ResponseEntity<List<ProductVO>> findAll() {
		List<ProductVO> products = service.findAll();
		return ResponseEntity.ok().body(products);
	}
	
	@GetMapping(value = "/{id}")
	private ResponseEntity<ProductVO> findById(@PathVariable Long id) {
		ProductVO product = service.findById(id);
		return ResponseEntity.ok().body(product);
	}
	
	@PostMapping
	private ResponseEntity<ProductVO> create(@RequestBody ProductVO product) {
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
	
	@PutMapping(value = "/{id}")
	private ResponseEntity<ProductVO> update(@PathVariable Long id, @RequestBody ProductVO product) {
		product = service.update(id, product);
		return ResponseEntity.ok().body(product);
	}
}
