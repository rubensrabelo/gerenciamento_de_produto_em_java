package com.management.product.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.management.product.models.Product;
import com.management.product.repositories.ProductRepository;
import com.management.product.unittest.mapper.mocks.MockProduct;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	
	MockProduct input;
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	ProductRepository repository;

	@BeforeEach
	void setUp() throws Exception {
		input = new MockProduct();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindAll() {
		Product product = input.mockEntity();
		product.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(product));
		
		var result = service.findById(1L);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());
		
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().endsWith("/products/1")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/products")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().endsWith("/products")
						&& link.getType().equals("POST")
						));
		
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().endsWith("/products")
						&& link.getType().equals("PUT")
						));
		
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().endsWith("/products")
						&& link.getType().equals("DELETE")
						));
		
		assertEquals("Name 1", product.getName());
		assertEquals(1D, product.getPrice());
	}

	@Test
	void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	void testCreate() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

}
