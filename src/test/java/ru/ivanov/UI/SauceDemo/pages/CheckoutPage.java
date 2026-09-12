package ru.ivanov.UI.SauceDemo.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class CheckoutPage {
    private final SelenideElement firstNameField = $("[data-test='firstName']");
    private final SelenideElement lastNameField = $("[data-test='lastName']");
    private final SelenideElement postalCodeField = $("[data-test='postalCode']");
    private final SelenideElement cancelButton = $("[data-test='cancel']");
    private final SelenideElement continueButton = $("[data-test='continue']");

    public CheckoutPage enterFirstName(String firstName) {
        firstNameField.setValue(firstName);
        return this;
    }
    public CheckoutPage enterLastName(String lastName) {
        lastNameField.setValue(lastName);
        return this;
    }
    public CheckoutPage enterPostalCode(String postalCode) {
        postalCodeField.setValue(postalCode);
        return this;
    }
    public CheckoutPage clickCancelButton() {
        cancelButton.click();
        return this;
    }
    public CheckoutPage clickContinueButton() {
        continueButton.click();
        return this;
    }
}
