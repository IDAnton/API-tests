package ru.ivanov.UI.SauceDemo.Steps;

import ru.ivanov.UI.SauceDemo.PageObjects.*;
import ru.ivanov.UI.SauceDemo.PageObjects.Items.InventoryItem;

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

    public SauceSteps addFirstItemToCart(int n) {
        inventoryPage.getAllProducts().stream().limit(n).forEach(InventoryItem::addToCart);
        return this;
    }

    public SauceSteps checkoutWithCredentials(String firstName, String lastName, String postalCode) {
        inventoryPage.clickCart();
        cartPage.clickCheckoutButton();
        checkoutPage.enterFirstName(firstName).enterLastName(lastName).enterPostalCode(postalCode).clickContinueButton();
        checkoutFinalPage.clickFinishButton();
        return this;
    }

    public boolean verifyCheckout() {
        return checkoutCompletePage.isComplete();
    }
}
