package ru.ivanov;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ivanov.models.AuthApi.AuthRequest;

import static org.hamcrest.Matchers.*;

import ru.ivanov.models.AuthApi.Bookingdates;
import ru.ivanov.models.AuthApi.CreateBookingRequest;
import ru.ivanov.tools.AuthSpecification;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class ApiAuthTest {

    private static String authToken;

    @BeforeAll
    public static void getCorrectAuthToken() {
        AuthRequest correctAuthRequest = new AuthRequest("admin", "password123");
        authToken = given()
                .spec(AuthSpecification.getAuthSpec())
                .body(correctAuthRequest)
                .when()
                .post()
                .then()
                .extract().body().path("token");
    }

    //Надо протестировать что токен который мы получили действительно рабочий:
    //Для этого можно создать бронирование (api позволяет это без токена) а затем попробовать его удалит (нужен токен).
    //Возможно это не очень хороший подход (нарушение принципа единой ответственности для этого теста) ?
    @Test
    @DisplayName("Проверка токена авторизации (создание и удаления записи)")
    public void testAuthToken() {
        int bookingId = given()
                .spec(AuthSpecification.postBookingSpec())
                .body(provideBookingRequest())
                .when()
                .post()
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract().body().path("bookingid");

        given()
                .spec(AuthSpecification.deleteBookingSpec(bookingId, authToken))
                .delete()
                .then()
                .log().ifValidationFails()
                .statusCode(201);
    }


    @ParameterizedTest(name = "{1}")
    @MethodSource("provideInvalidAuthData")
    public void testAuthWithBadCredentials(AuthRequest invalidBody, String testName) {
        given()
                .spec(AuthSpecification.getAuthSpec())
                .body(invalidBody)
                .when()
                .post()
                .then()
                .log().ifValidationFails()
                .statusCode(401)
                .body("reason", is("Bad credentials"));
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("provideInvalidBody")
    public void testAuthWithInvalidBody(String invalidBody, ContentType contentType, String testName) {
        given()
                .spec(AuthSpecification.getAuthSpec())
                .contentType(contentType)
                .body(invalidBody)
                .when()
                .post()
                .then()
                .log().ifValidationFails()
                .statusCode(400);
    }


    private static Stream<Arguments> provideInvalidAuthData() {
        return Stream.of(
                Arguments.of(new AuthRequest("anton", "password123"), "Неправильный логин"),
                Arguments.of(new AuthRequest("not_admin", "password123"), "Неверный логин"),
                Arguments.of(new AuthRequest("anton", "password"), "Неправильный пароль"),
                Arguments.of(new AuthRequest("Admin", "password123"), "Неверный регистр логина"),
                Arguments.of(new AuthRequest("admin", "Password123"), "Неверный регистр пароля"),
                Arguments.of(new AuthRequest("", "password123"), "Пустой логин"),
                Arguments.of(new AuthRequest("admin", ""), "Пустой пароль"),
                Arguments.of(new AuthRequest("", ""), "Пустые поля")
        );
    }

    private static Stream<Arguments> provideInvalidBody() {
        return Stream.of(
                Arguments.of("{}", ContentType.JSON, "Пустой JSON объект"),
                Arguments.of("", ContentType.TEXT, "Пустое тело"),
                Arguments.of("{username", ContentType.JSON, "Невалидный JSON")
        );
    }

    private static CreateBookingRequest provideBookingRequest() {
        return new CreateBookingRequest("Anton", "Ivanov", BigDecimal.valueOf(1000), true,
                new Bookingdates("2026-01-01", "2026-01-10"), "Breakfast");
    }
}
