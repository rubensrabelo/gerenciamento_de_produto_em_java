package com.management.product.repositories;

import com.management.product.integrationtest.testcontainers.AbstractIntegrationTest;
import com.management.product.models.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    ProductRepository repository;
    private static Product product;

    @BeforeAll
    static void setUp() {
        product = new Product();
    }

    @Test
    @Order(1)
    void findProductByName() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.ASC, "name")
        );

        product = repository.findProductByName("de", pageable).getContent().get(1);

        assertNotNull(product);
        assertNotNull(product.getId());
        assertTrue(product.getId() > 0);

        assertEquals("Fone de ouvido Sony WH-1000XM4", product.getName());
        assertEquals(2299.0, product.getPrice());
        Assertions.assertTrue(product.getInStock());
    }

    @Test
    @Order(2)
    void productIsNotInStock() {

        Long id = product.getId();
        repository.productIsNotInStock(id);


        var result = repository.findById(id);
        product = result.get();

        assertNotNull(product);
        assertNotNull(product.getId());
        assertTrue(product.getId() > 0);

        assertEquals("Fone de ouvido Sony WH-1000XM4", product.getName());
        assertEquals(2299.0, product.getPrice());
        Assertions.assertFalse(product.getInStock());
    }

}