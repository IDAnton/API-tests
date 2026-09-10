package ru.ivanov.API;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.ivanov.API.models.Api.UserRequest;
import ru.ivanov.API.models.Api.UserResponse;
import org.assertj.core.api.SoftAssertions;
import ru.ivanov.API.tools.ApiRequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ApiTest {
    @ParameterizedTest(name = "Тест получения списка пользователей для страницы page={0}")
    @ValueSource(ints = {0, 1, 2})
    @DisplayName("Тест получения списка пользователей при разных номерах страниц")
    public void testUsersList(int page) {
        given()
                .spec(ApiRequestSpecification.getRequestSpec())
                .queryParam("page", page)
                .when()
                .get("/users")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("API/users_list_schema.json"));
    }

    @Test
    @DisplayName("Тест создания пользователя")
    public void testUserCreation() {
        UserRequest requestBody = new UserRequest("Anton", "QA");
        UserResponse responseBody = given()
                .spec(ApiRequestSpecification.getRequestSpec())
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("API/create_user_schema.json"))
                .extract().as(UserResponse.class);

        try {
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(responseBody)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "createdAt")
                        .isEqualTo(requestBody);

                softly.assertThat(responseBody.id()).as("User ID").isNotBlank();
                softly.assertThat(responseBody.createdAt()).as("Created Date").isNotBlank();
            });
        } finally {
            given()
                    .spec(ApiRequestSpecification.getRequestSpec())
                    .when()
                    .delete("/users/" + responseBody.id())
                    .then()
                    .statusCode(204);
        }
    }
}
