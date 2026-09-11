package ru.ivanov.UI.SauceDemo.PageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.ivanov.UI.SauceDemo.PageObjects.Items.CartItem;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CheckoutFinalPage implements CartItemsHolder {
    private final SelenideElement cancelButton = $("[data-test='cancel']");
    private final SelenideElement finishButton = $("[data-test='finish']");
    private final ElementsCollection cartItems = $$("[data-test='inventory-item']");
    private final SelenideElement paymentInfo = $("[data-test='payment-info-value']");
    private final SelenideElement shippingInfo = $("[data-test='shipping-info-value']");
    private final SelenideElement totalPrice = $("[data-test='total-label']");

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItems.stream()
                .map(CartItem::new)
                .toList();
    }

    public CheckoutFinalPage clickCancelButton() {
        cancelButton.click();
        return this;
    }

    public CheckoutFinalPage clickFinishButton() {
        finishButton.click();
        return this;
    }

    public String getPaymentInfoText() {
        return paymentInfo.getText();
    }

    public String getShippingInfoText() {
        return shippingInfo.getText();
    }

    public String getTotalPrice() {
        return totalPrice.getText();
    }
}
