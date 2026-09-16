package ru.ivanov.UI.Login;

import com.codeborne.selenide.Configuration;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


public class LoginTest {
    static LoginConfiguration loginConfig;

    @BeforeAll
    static void setup() {
        loginConfig = ConfigFactory.create(LoginConfiguration.class);
        Configuration.baseUrl = loginConfig.baseUrl();
    }

    @ParameterizedTest(name = "Тест авторизации {3}")
    @MethodSource("loginDataProvider")
    void testLoginNegativeScenarios(String username, String password, String expectedText, String testName) {
        LoginSteps loginSteps = new LoginSteps(loginConfig.loginUrl());
        boolean loginResult = loginSteps.loginWithCredentials(username, password).isLogged(expectedText);
        Assertions.assertTrue(loginResult);
    }

    static Stream<Arguments> loginDataProvider() {
        String usernameMsg = "Your username is invalid!";
        String passwordMsg = "Your password is invalid!";
        String successMsg = "You logged into a secure area!";

        return Stream.of(
                Arguments.of("?/!@#$%^&*", "?/!@#$%^&*", usernameMsg, "Спецсимволы"),
                Arguments.of("test", "' OR 1=1 --", usernameMsg, "SQL инъекция"),
                Arguments.of("", "", usernameMsg, "Пустые поля"),
                Arguments.of("a", "b", usernameMsg, "Один символ"),
                Arguments.of("tomsmith", "123", passwordMsg, "Правильный логин, неверный пароль"),
                Arguments.of("tomsmith123", "SuperSecretPassword!", usernameMsg, "Правильный пароль, неверный логин"),
                Arguments.of("tomsmith", "SuperSecretPassword!", successMsg, "Правильный логин пароль")
        );
    }
}