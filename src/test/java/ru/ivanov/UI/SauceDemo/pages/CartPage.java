package ru.ivanov.UI.SauceDemo.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.ivanov.UI.SauceDemo.pages.items.CartItem;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {
    private final SelenideElement inventoryLink = $("[data-test='continue-shopping']");
    private final SelenideElement checkoutLink = $("[data-test='checkout']");
    private final ElementsCollection cartItems = $$("[data-test='inventory-item]'");

    public List<CartItem> getAllItems() {
        return cartItems.stream()
                .map(CartItem::new)
                .toList();
    }

    public CartPage clickCheckoutButton() {
        checkoutLink.click();
        return this;
    }

    public CartPage clickBackToInventory() {
        inventoryLink.click();
        return this;
    }
}
