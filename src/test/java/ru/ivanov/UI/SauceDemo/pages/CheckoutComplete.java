package ru.ivanov.UI.SauceDemo.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class CheckoutComplete {
    private final SelenideElement completeContainer = $("[data-test='checkout-complete-container']");
    private final SelenideElement backHomeButton = $("[data-test='back-to-products']");
    private final SelenideElement pdfButton = $("[data-test='generate-pdf-order']");

    public boolean isComplete() {
        return completeContainer.shouldBe(Condition.visible).isDisplayed();
    }
}
