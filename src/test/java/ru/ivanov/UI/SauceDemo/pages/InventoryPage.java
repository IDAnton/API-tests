package ru.ivanov.UI.SauceDemo.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.ivanov.UI.SauceDemo.pages.items.ShopItem;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class InventoryPage {
    private final ElementsCollection inventoryItems = $$("[data-test='inventory-item']");
    private final SelenideElement shoppingCartLink = $("[data-test='shopping-cart-link']");

    public Stream<ShopItem> getAllProducts() {
        return inventoryItems.stream().map(ShopItem::new);
    }

    public ShopItem getProductByName(String productName) {
        return inventoryItems.stream()
                .map(ShopItem::new)
                .filter(shopItem -> shopItem.getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Продукт с именем '%s' не найден".formatted(productName)));
    }

    public InventoryPage clickCart() {
        shoppingCartLink.click();
        return this;
    }
}
