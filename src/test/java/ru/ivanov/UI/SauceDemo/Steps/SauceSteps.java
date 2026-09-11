package ru.ivanov.UI.SauceDemo.Steps;

import ru.ivanov.UI.SauceDemo.PageObjects.*;
import ru.ivanov.UI.SauceDemo.PageObjects.Items.CartItem;
import ru.ivanov.UI.SauceDemo.PageObjects.Items.ShopItem;

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

    public List<ShopItem> addFirstItemsToCart(int n) {
        inventoryPage.getAllProducts().stream().limit(n).forEach(ShopItem::addToCart);
        return inventoryPage.getAllProducts().stream().limit(n).toList();
    }

    public SauceSteps checkoutWithCredentials(String firstName, String lastName, String postalCode) {
        inventoryPage.clickCart();
        cartPage.clickCheckoutButton();
        checkoutPage.enterFirstName(firstName).enterLastName(lastName).enterPostalCode(postalCode).clickContinueButton();
        return this;
    }

    public List<CartItem> getFinalCartItems() {
        return checkoutFinalPage.getAllCartItems();
    }

    public boolean finishAndVerifyCheckout() {
        checkoutFinalPage.clickFinishButton();
        return checkoutCompletePage.isComplete();
    }
}
