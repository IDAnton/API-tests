package ru.ivanov.UI.SauceDemo.PageObjects;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPageSelenide {
    private final SelenideElement usernameInput = $("[data-test='username']");
    private final SelenideElement passwordInput = $("[data-test='password']");
    private final SelenideElement loginButton = $("[data-test='login-button']");
    private final SelenideElement inventoryContainer = $("[data-test='inventory-container']");
    private final SelenideElement errorMessage = $("[data-test='error']");
    private final String baseUrl;

    public LoginPageSelenide(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public LoginPageSelenide openLoginPage() {
        open(baseUrl);
        return this;
    }

    public LoginPageSelenide login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        loginButton.click();
        return this;
    }

    public LoginPageSelenide checkLoggedIn() {
        inventoryContainer.shouldBe(visible);
        return this;
    }

    public LoginPageSelenide checkErrorMessageText(String errorText) {
        errorMessage.shouldHave(text(errorText));
        return this;
    }
}
