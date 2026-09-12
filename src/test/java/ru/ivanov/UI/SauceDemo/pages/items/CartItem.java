package ru.ivanov.UI.SauceDemo.pages.items;

import com.codeborne.selenide.SelenideElement;
import ru.ivanov.UI.SauceDemo.pages.dto.Item;

public class CartItem implements InventoryItem {
    private final SelenideElement parentElement;
    private final String removeButton = "[data-test*='remove-sauce']";
    private final String itemNameField = "[data-test='inventory-item-name']";
    private final String itemPriceField = "[data-test='inventory-item-price']";
    private final String itemQuantityField = "[data-test='item-quantity']";

    public CartItem(SelenideElement parentElement) {
        this.parentElement = parentElement;
    }

    public String getItemQuantityField() {
        return parentElement.$(itemQuantityField).getText();
    }

    public String getItemPriceField() {
        return parentElement.$(itemPriceField).getText();
    }

    public String getItemNameField() {
        return parentElement.$(itemNameField).getText();
    }

    public void clickRemoveButton() {
        parentElement.$(removeButton).click();
    }

    @Override
    public Item toData() {
        return new Item(getItemNameField(), getItemPriceField());
    }
}
