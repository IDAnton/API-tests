package ru.ivanov.UI.SauceDemo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.ivanov.UI.SauceDemo.pages.LoginPageSelenium;

public class SauceDemoSeleniumTest {
    private final String loginErrorMessage = "Epic sadface: Username and password do not match any user in this service";

    private WebDriver driver;
    private LoginPageSelenium loginPage;

    @BeforeEach
    void setUpDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        loginPage = new LoginPageSelenium(driver);
    }

    @AfterEach
    void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Успешная авторизация в standard_user")
    void testSuccessfulLogin() {
        loginPage.openLoginPage();
        loginPage.login("standard_user", "secret_sauce");
        Assertions.assertThat(loginPage.isInventoryDisplayed())
                .as("Каталог товаров не найден")
                .isTrue();
    }

    @Test
    @DisplayName("Неверный пароль для standard_user")
    void testWrongPassword() {
        loginPage.openLoginPage();
        loginPage.login("standard_user", "password");
        Assertions.assertThat(loginPage.getErrorMessageText())
                .as("Ошибка авторизации")
                .contains(loginErrorMessage);
    }
}