package ru.ivanov.UI.Login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @BeforeAll
    static void setup() {
        Configuration.baseUrl = "https://the-internet.herokuapp.com/";
    }

    @ParameterizedTest(name = "Тест авторизации {3}")
    @MethodSource("loginDataProvider")
    void testLoginNegativeScenarios(String username, String password, String expectedError, String testName) {
        open("/login");
        $("#username").setValue(username);
        $("#password").setValue(password);
        $("button[type='submit']").click();

        $("#flash")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(expectedError));
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