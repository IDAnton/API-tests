package ru.ivanov.UI.SauceDemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPageSelenium {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl = "https://www.saucedemo.com/";
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By inventoryContainer = By.id("inventory_container");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPageSelenium(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openLoginPage() {
        driver.get(baseUrl);
    }

    public void login(String username, String password) {
        WebElement loginField = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        loginField.clear();
        loginField.sendKeys(username);

        WebElement passwordFiled = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passwordFiled.clear();
        passwordFiled.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public boolean isInventoryDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryContainer)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }
}
