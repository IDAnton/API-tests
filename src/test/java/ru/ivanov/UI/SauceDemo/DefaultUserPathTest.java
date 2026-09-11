package ru.ivanov.UI.SauceDemo;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import ru.ivanov.UI.SauceDemo.Steps.SauceSteps;
import ru.ivanov.UI.SauceDemo.tools.Configuration;


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
        boolean result = steps
                .login(configuration.username(), configuration.password())
                .addFirstItemToCart(3)
                .checkoutWithCredentials("Anton", "Ivanov", "880000")
                .verifyCheckout();
        Assertions.assertTrue(result, "Не удалось купить товары");
    }
}
