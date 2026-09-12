package ru.ivanov.UI.SauceDemo.pages.items;

import com.codeborne.selenide.SelenideElement;
import ru.ivanov.UI.SauceDemo.pages.dto.Item;

public class ShopItem implements InventoryItem {
    private final SelenideElement parentElement;

    public ShopItem(SelenideElement rootElement) {
        this.parentElement = rootElement;
    }

    public String getName() {
        return parentElement.$("[data-test='inventory-item-name']").getText();
    }

    public String getPrice() {
        return parentElement.$("[data-test='inventory-item-price']").getText();
    }

    public ShopItem addToCart() {
        parentElement.$("[data-test*='add-to-cart']").click();
        return this;
    }

    @Override
    public Item toData() {
        return new Item(getName(), getPrice());
    }
}