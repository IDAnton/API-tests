package ru.ivanov.UI.SauceDemo;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ivanov.UI.SauceDemo.PageObjects.LoginPageSelenide;


public class SauceDemoSelenideTest {
    private final String loginErrorMessage = "Epic sadface: Username and password do not match any user in this service";
    private LoginPageSelenide loginPage;

    @BeforeAll
    static void config() {
        Configuration.headless = true;
    }

    @BeforeEach
    void setUp() {
        loginPage = new LoginPageSelenide();
    }

    @Test
    @DisplayName("Успешная авторизация в standard_user")
    void testSuccessfulLogin() {
        loginPage.openLoginPage()
                .login("standard_user", "secret_sauce")
                .checkLoggedIn();
    }

    @Test
    @DisplayName("Неверный пароль для standard_user")
    void testWrongPassword() {
        loginPage.openLoginPage()
                .login("standard_user", "password")
                .checkErrorMessageText(loginErrorMessage);
    }
}