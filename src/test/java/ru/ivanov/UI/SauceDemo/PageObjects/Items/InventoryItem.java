package ru.ivanov.UI.SauceDemo.PageObjects.Items;

import com.codeborne.selenide.SelenideElement;

public class InventoryItem {
    private final SelenideElement parentElement;

    public InventoryItem(SelenideElement rootElement) {
        this.parentElement = rootElement;
    }

    public String getName() {
        return parentElement.$("[data-test='inventory-item-name']").getText();
    }

    public String getPrice() {
        return parentElement.$("[data-test='inventory-item-price']").getText();
    }

    public void addToCart() {
        parentElement.$("[data-test*='add-to-cart']").click();
    }
}