package com.management.product.integrationtest.controllers.cors.withjson;

import com.management.product.config.TestConfigs;
import com.management.product.integrationtest.dto.ProductDTO;
import com.management.product.integrationtest.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerCorsTest extends AbstractIntegrationTest {

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
    void createWithWrongOrigin() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
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
                    .statusCode(403)
                .extract()
                    .body()
                .asString();

        assertEquals("Invalid CORS request", content);
    }


    @Test
    @Order(3)
    void findById() throws IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBasePath("/products")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

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

        assertEquals("Product 01", createdProduct.getName());
        assertEquals(1.00, createdProduct.getPrice());
        Assertions.assertTrue(createdProduct.getInStock());
    }

    @Test
    @Order(4)
    void findByIdWithWrongOrigin() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("/products")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParams("id", product.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();
        assertEquals("Invalid CORS request", content);
    }

    private void mockProduct() {
        product.setName("Product 01");
        product.setPrice(1.00);
        product.setInStock(true);
    }
}