package ru.ivanov.UI.SauceDemo;

import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import ru.ivanov.UI.SauceDemo.PageObjects.DTO.Item;
import ru.ivanov.UI.SauceDemo.PageObjects.Items.CartItem;
import ru.ivanov.UI.SauceDemo.PageObjects.Items.InventoryItem;
import ru.ivanov.UI.SauceDemo.Steps.SauceSteps;
import ru.ivanov.UI.SauceDemo.tools.Configuration;

import java.util.List;


public class DefaultUserPathTest {
    private static Configuration configuration;

    @BeforeAll
    public static void setup() {
        configuration = ConfigFactory.create(Configuration.class);
    }

    @Test
    @DisplayName("Логин и покупка товаров")
    public void defaultUserPathTest() {
        SauceSteps steps = new SauceSteps(configuration.url());
        List<Item> shopItems = steps.login(configuration.username(), configuration.password()).addFirstItemsToCart(2)
                .stream()
                .map(InventoryItem::toData)
                .toList();

        steps.checkoutWithCredentials("Anton", "Ivanov", "880000");

        List<Item> addedItems = steps.getFinalCartItems()
                .stream()
                .map(CartItem::toData)
                .toList();

        boolean result = steps.finishAndVerifyCheckout();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(shopItems)
                    .containsExactlyInAnyOrderElementsOf(addedItems);
            softly.assertThat(result).isEqualTo(true);
        });
    }
}
