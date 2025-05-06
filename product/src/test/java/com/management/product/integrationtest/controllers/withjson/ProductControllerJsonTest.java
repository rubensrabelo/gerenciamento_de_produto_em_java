package com.management.product.integrationtest.controllers.withjson;

import com.management.product.config.TestConfigs;
import com.management.product.integrationtest.dto.ProductDTO;
import com.management.product.integrationtest.dto.wrappers.json.WrapperProductDTO;
import com.management.product.integrationtest.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static ProductDTO product;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        product = new ProductDTO();
    }

    @Test
    @Order(1)
    void create() throws IOException {
        mockProduct();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBasePath("/products")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(product)
                .when()
                    .post()
                .then()
                    .statusCode(201)
                .extract()
                    .body()
                    .asString();

        ProductDTO createdProduct = objectMapper.readValue(content, ProductDTO.class);
        product = createdProduct;

        assertNotNull(createdProduct.getId());
        assertNotNull(createdProduct.getName());
        assertNotNull(createdProduct.getPrice());

        Assertions.assertTrue(createdProduct.getId() > 0);

        assertEquals("Product 01", createdProduct.getName());
        assertEquals(1.00, createdProduct.getPrice());
        Assertions.assertTrue(createdProduct.getInStock());
    }

    @Test
    @Order(2)
    void update() throws IOException {
        product.setName("Product Updated");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(product)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        ProductDTO createdProduct = objectMapper.readValue(content, ProductDTO.class);
        product = createdProduct;

        assertNotNull(createdProduct.getId());
        assertNotNull(createdProduct.getName());
        assertNotNull(createdProduct.getPrice());

        Assertions.assertTrue(createdProduct.getId() > 0);

        assertEquals("Product Updated", createdProduct.getName());
        assertEquals(1.00, createdProduct.getPrice());
        Assertions.assertTrue(createdProduct.getInStock());
    }


    @Test
    @Order(3)
    void findById() throws IOException {
        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParams("id", product.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        ProductDTO createdProduct = objectMapper.readValue(content, ProductDTO.class);

        assertNotNull(createdProduct.getId());
        assertNotNull(createdProduct.getName());
        assertNotNull(createdProduct.getPrice());

        Assertions.assertTrue(createdProduct.getId() > 0);

        assertEquals("Product Updated", createdProduct.getName());
        assertEquals(1.00, createdProduct.getPrice());
        Assertions.assertTrue(createdProduct.getInStock());
    }

    @Test
    @Order(4)
    void productIsNotInStock() throws IOException {
        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParams("id", product.getId())
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        ProductDTO createdProduct = objectMapper.readValue(content, ProductDTO.class);

        assertNotNull(createdProduct.getId());
        assertNotNull(createdProduct.getName());
        assertNotNull(createdProduct.getPrice());

        Assertions.assertTrue(createdProduct.getId() > 0);

        assertEquals("Product Updated", createdProduct.getName());
        assertEquals(1.00, createdProduct.getPrice());
        Assertions.assertFalse(createdProduct.getInStock());
    }

    @Test
    @Order(5)
    void delete() {

        given(specification)
                .pathParam("id", product.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void findAll() throws IOException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        // List<ProductDTO> products = objectMapper.readValue(content, new TypeReference<List<ProductDTO>>() {});
        WrapperProductDTO wrapper = objectMapper.readValue(content, WrapperProductDTO.class);
        var products = wrapper.getEmbedded().getProducts();

        ProductDTO productOne = products.get(1);

        assertNotNull(productOne.getId());
        assertTrue(productOne.getId() > 0);

        assertEquals("Cafeteira Nespresso", productOne.getName());
        assertEquals(699.9, productOne.getPrice());
        Assertions.assertTrue(productOne.getInStock());

        ProductDTO productFour = products.get(4);

        assertNotNull(productFour.getId());
        assertTrue(productFour.getId() > 0);

        assertEquals("Laptop Dell Inspiron", productFour.getName());
        assertEquals(1500.0, productFour.getPrice());
        Assertions.assertTrue(productFour.getInStock());
    }

    @Test
    @Order(7)
    void findProductByName() throws IOException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("name", "de")
                .queryParam("page", 0, "size", 10, "direction", "asc")
                .when()
                .get("search/{name}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperProductDTO wrapper = objectMapper.readValue(content, WrapperProductDTO.class);
        var products = wrapper.getEmbedded().getProducts();

        ProductDTO productOne = products.get(1);

        assertNotNull(productOne.getId());
        assertTrue(productOne.getId() > 0);

        assertEquals("Fone de ouvido Sony WH-1000XM4", productOne.getName());
        assertEquals(2299.0, productOne.getPrice());
        Assertions.assertTrue(productOne.getInStock());

        ProductDTO productFour = products.get(4);

        assertNotNull(productFour.getId());
        assertTrue(productFour.getId() > 0);

        assertEquals("Máquina de Lavar Samsung 10kg", productFour.getName());
        assertEquals(1999.0, productFour.getPrice());
        Assertions.assertTrue(productFour.getInStock());
    }

    private void mockProduct() {
        product.setName("Product 01");
        product.setPrice(1.00);
        product.setInStock(true);
    }
}