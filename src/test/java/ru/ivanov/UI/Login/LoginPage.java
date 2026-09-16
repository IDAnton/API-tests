package ru.ivanov.UI.Login;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {
    private final SelenideElement usernameFiled = $("#username");
    private final SelenideElement passwordFiled = $("#password");
    private final SelenideElement flashField = $("#flash");
    private final SelenideElement loginButton = $("button[type='submit']");

    LoginPage(String loginPageUrl) {
        open(loginPageUrl);
    }

    public LoginPage setUsername(String username) {
        usernameFiled.setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordFiled.setValue(password);
        return this;
    }

    public LoginPage clickLoginButton() {
        loginButton.click();
        return this;
    }

    boolean flashFieldHasText(String expectedError) {
        return flashField
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(expectedError))
                .isDisplayed();
    }
}
