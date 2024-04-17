package com.example.azurefunctiontc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@Import(TestContainersConfiguration.class)
@SpringBootTest
class MyAzureFunctionIT {

    @Autowired
    @Qualifier("function")
    private GenericContainer<?> function;

    @Test
    void shouldReturnUppercase() {

        var response = given()
                .body("testcontainers")
                .when()
                .post(String.format("http://localhost:%s/api/uppercase", function.getMappedPort(80)))
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        assertThat(response)
                .isNotNull()
                .isEqualTo("TESTCONTAINERS");
    }
}
