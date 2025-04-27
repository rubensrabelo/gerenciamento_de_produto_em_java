package com.management.product.unittest.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.management.product.services.ProductService;
import com.management.product.services.exceptions.RequiredObjectIsNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.management.product.data.dto.ProductDTO;
import com.management.product.models.Product;
import com.management.product.repositories.ProductRepository;
import com.management.product.unittest.mapper.mocks.MockProduct;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
	@Disabled("Reason: Still under development")
	void testFindAll() {
        List<Product> list = input.mockEntityList(14);
		when(repository.findAll()).thenReturn(list);
		List<ProductDTO> prods = service.findAll();

		assertNotNull(prods);
		assertEquals(14, prods.size());

		var prodOne = prods.get(1);

		assertNotNull(prodOne);
		assertNotNull(prodOne.getId());
		assertNotNull(prodOne.getLinks());

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().equals("/products/1")
				&& link.getType().equals("GET")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().equals("/products/")
						&& link.getType().equals("GET")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().equals("/product/1")
						&& link.getType().equals("POST")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().equals("/product/1")
						&& link.getType().equals("PUT")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().equals("/product/1")
						&& link.getType().equals("DELETE")));

		assertEquals("Name 1", prodOne.getName());
		assertEquals(1D, prodOne.getPrice());


		var prodFour = prods.get(4);

		assertNotNull(prodFour);
		assertNotNull(prodFour.getId());
		assertNotNull(prodFour.getLinks());

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().equals("/products/1")
						&& link.getType().equals("GET")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().equals("/products/")
						&& link.getType().equals("GET")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().equals("/product/1")
						&& link.getType().equals("POST")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().equals("/product/1")
						&& link.getType().equals("PUT")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().equals("/product/1")
						&& link.getType().equals("DELETE")));

		assertEquals("Name 4", prodFour.getName());
		assertEquals(4D, prodFour.getPrice());


		var prodSeven = prods.get(7);

		assertNotNull(prodSeven);
		assertNotNull(prodSeven.getId());
		assertNotNull(prodSeven.getLinks());

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("self")
						&& link.getHref().equals("/products/1")
						&& link.getType().equals("GET")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().equals("/products/")
						&& link.getType().equals("GET")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("create")
						&& link.getHref().equals("/product/1")
						&& link.getType().equals("POST")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("update")
						&& link.getHref().equals("/product/1")
						&& link.getType().equals("PUT")));

		assertNotNull(prodOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("delete")
						&& link.getHref().equals("/product/1")
						&& link.getType().equals("DELETE")));

		assertEquals("Name 7", prodSeven.getName());
		assertEquals(7D, prodSeven.getPrice());
    }

	@Test
	void testFindById() {
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
	void testCreate() {
		Product product = input.mockEntity(1);
		Product persisted = product;
		
		ProductDTO dto = input.mockDTO(1);
		
		when(repository.save(product)).thenReturn(persisted);
		
		var result = service.create(dto);
		
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
	void testCreateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});

		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete() {
		Product product = input.mockEntity(1);
		
		when(repository.findById(1L)).thenReturn(Optional.of(product));
		
		service.delete(1L);
		
		verify(repository, times(1)).findById(anyLong());
		verify(repository, times(1)).delete(any(Product.class));
		verifyNoMoreInteractions(repository);
	}

	@Test
	void testUpdate() {
		Product product = input.mockEntity(1);
		Product persisted = product;
		
		ProductDTO dto = input.mockDTO(1);
		
		when(repository.findById(1L)).thenReturn(Optional.of(product));
		when(repository.save(product)).thenReturn(persisted);
		
		var result = service.update(dto);
		
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
	void testUpdateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});

		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
}
