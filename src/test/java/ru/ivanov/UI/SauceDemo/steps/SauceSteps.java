package ru.ivanov.UI.SauceDemo.steps;

import ru.ivanov.UI.SauceDemo.pages.*;
import ru.ivanov.UI.SauceDemo.pages.dto.Item;
import ru.ivanov.UI.SauceDemo.pages.items.CartItem;
import ru.ivanov.UI.SauceDemo.pages.items.ShopItem;

import java.util.List;

public class SauceSteps {
    private final LoginPageSelenide loginPage;
    private final InventoryPage inventoryPage = new InventoryPage();
    private final CartPage cartPage = new CartPage();
    private final CheckoutPage checkoutPage = new CheckoutPage();
    private final CheckoutFinalPage checkoutFinalPage = new CheckoutFinalPage();
    private final CheckoutComplete checkoutCompletePage = new CheckoutComplete();

    public SauceSteps(String baseUrl) {
        loginPage = new LoginPageSelenide(baseUrl);
    }

    public SauceSteps login(String email, String password) {
        loginPage.openLoginPage().login(email, password);
        return this;
    }

    public List<Item> addFirstItemsToCart(int n) {
        return inventoryPage.getAllProducts()
                .limit(n)
                .map(ShopItem::addToCart)
                .map(ShopItem::toData)
                .toList();
    }

    public SauceSteps checkoutWithCredentials(String firstName, String lastName, String postalCode) {
        inventoryPage.clickCart();
        cartPage.clickCheckoutButton();
        checkoutPage.enterFirstName(firstName).enterLastName(lastName).enterPostalCode(postalCode).clickContinueButton();
        return this;
    }

    public List<Item> getFinalCartItems() {
        return checkoutFinalPage.getAllItems().map(CartItem::toData).toList();
    }

    public boolean finishAndVerifyCheckout() {
        checkoutFinalPage.clickFinishButton();
        return checkoutCompletePage.isComplete();
    }
}
