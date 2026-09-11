package ru.ivanov.UI.SauceDemo;

import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import ru.ivanov.UI.SauceDemo.PageObjects.DTO.Item;
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
        List<Item> shopItems = steps.login(configuration.username(), configuration.password()).addFirstItemsToCart(2);
        steps.checkoutWithCredentials("Anton", "Ivanov", "880000");
        List<Item> addedItems = steps.getFinalCartItems();
        boolean purchaseResult = steps.finishAndVerifyCheckout();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(shopItems)
                    .containsExactlyInAnyOrderElementsOf(addedItems); // Проверка, что мы действительно покупаем товары которые выбрали
            softly.assertThat(purchaseResult).isEqualTo(true); // Проверка, что отобразилась финальная страница покупки
        });
    }
}
