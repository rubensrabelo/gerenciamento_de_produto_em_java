package com.management.product.integrationtest.swagger;

import com.management.product.config.TestConfigs;
import com.management.product.integrationtest.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldDisplaySwaggerUIPage() {
        var content = given()
                .basePath("/swagger-ui/index.html")
                    .port(TestConfigs.SERVER_PORT)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                .asString();
        assertTrue(content.contains("Swagger UI"));
    }
}
