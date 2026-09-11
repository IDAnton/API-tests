package ru.ivanov.UI.SauceDemo.PageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.ivanov.UI.SauceDemo.PageObjects.Items.InventoryItem;

import java.util.List;
import java.util.Optional;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class InventoryPage {
    private final ElementsCollection inventoryItems = $$("[data-test='inventory-item']");
    private final SelenideElement shoppingCartLink = $("[data-test='shopping-cart-link']");

    public List<InventoryItem> getAllProducts() {
        return inventoryItems.stream()
                .map(InventoryItem::new)
                .toList();
    }

    public Optional<InventoryItem> getProductByName(String productName) {
        return inventoryItems.stream()
                .map(InventoryItem::new)
                .filter(inventoryItem -> inventoryItem.getName().equals(productName))
                .findFirst();
    }

    public InventoryPage clickCart() {
        shoppingCartLink.click();
        return this;
    }
}
