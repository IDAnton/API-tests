package ru.ivanov.UI.SauceDemo.PageObjects.Items;

import com.codeborne.selenide.SelenideElement;

public class CartItem {
    private final SelenideElement parentElement;
    private final String removeButton = "[data-test*='remove-sauce']";
    private final String itemName = "[data-test='inventory-item-name']";
    private final String itemPrice = "[data-test='inventory-item-price']";
    private final String itemQuantity = "[data-test='item-quantity']";

    public CartItem(SelenideElement  parentElement) {
        this.parentElement = parentElement;
    }

    public String getItemQuantity() {
        return parentElement.$(itemQuantity).getText();
    }

    public String getItemPrice() {
        return parentElement.$(itemPrice).getText();
    }

    public String getItemName() {
        return parentElement.$(itemName).getText();
    }

    public void clickRemoveButton() {
        parentElement.$(removeButton).click();
    }
}
