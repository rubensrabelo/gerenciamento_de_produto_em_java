package com.management.product.integrationtest.controllers.withyaml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.management.product.config.TestConfigs;
import com.management.product.integrationtest.controllers.withyaml.mapper.YAMLMapper;
import com.management.product.integrationtest.dto.ProductDTO;
import com.management.product.integrationtest.dto.wrappers.xml.PagedModelProduct;
import com.management.product.integrationtest.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;
    private static ProductDTO product;

    @BeforeAll
    static void setUp() {
        objectMapper = new YAMLMapper();
        product = new ProductDTO();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBasePath("/products")
                .setPort(TestConfigs.SERVER_PORT)
                .setConfig(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)))
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void create() throws IOException {
        mockProduct();

        var createdProduct = given(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(product, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(201)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(ProductDTO.class, objectMapper);

        product = createdProduct;

        assertNotNull(createdProduct.getId());
        assertEquals("Product 01", createdProduct.getName());
        assertEquals(1.00, createdProduct.getPrice());
        assertTrue(createdProduct.getInStock());
    }

    @Test
    @Order(2)
    void update() throws IOException {
        product.setName("Product Updated");

        var updatedProduct = given(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(product, objectMapper)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(ProductDTO.class, objectMapper);

        product = updatedProduct;

        assertNotNull(updatedProduct.getId());
        assertEquals("Product Updated", updatedProduct.getName());
        assertEquals(1.00, updatedProduct.getPrice());
        assertTrue(updatedProduct.getInStock());
    }

    @Test
    @Order(3)
    void findById() throws IOException {
        var foundProduct = given(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParams("id", product.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(ProductDTO.class, objectMapper);

        assertNotNull(foundProduct.getId());
        assertEquals("Product Updated", foundProduct.getName());
        assertEquals(1.00, foundProduct.getPrice());
        assertTrue(foundProduct.getInStock());
    }

    @Test
    @Order(4)
    void productIsNotInStock() throws IOException {
        var updatedProduct = given(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParams("id", product.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(ProductDTO.class, objectMapper);

        assertNotNull(updatedProduct.getId());
        assertEquals("Product Updated", updatedProduct.getName());
        assertEquals(1.00, updatedProduct.getPrice());
        assertFalse(updatedProduct.getInStock());
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
        String content = given(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParam("page", 0, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .asString();

        JsonNode root = objectMapper.getMapper().readTree(content);
        JsonNode contentNode = root.get("content");

        List<ProductDTO> products = objectMapper.getMapper()
                .readValue(contentNode.toString(), new TypeReference<>() {});

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
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParams("name", "de")
                .queryParam("page", 0, "size", 10, "direction", "asc")
                .when()
                .get("search/{name}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .asString();

        JsonNode root = objectMapper.getMapper().readTree(content);
        JsonNode contentNode = root.get("content");

        List<ProductDTO> products = objectMapper.getMapper()
                .readValue(contentNode.toString(), new TypeReference<>() {});

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

    private static void mockProduct() {
        product.setName("Product 01");
        product.setPrice(1.00);
        product.setInStock(true);
    }
}
