package ru.ivanov.UI.LoadingPage;

import com.codeborne.selenide.Configuration;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.ivanov.UI.TheInternet.TheInternetConfiguration;

public class LoadingPageTest {
    static String loadedColor = "rgba(34, 34, 34, 1)";
    static TheInternetConfiguration config;

    @BeforeAll
    static void setup() {
        config = ConfigFactory.create(TheInternetConfiguration.class);
        Configuration.baseUrl = config.baseUrl();
    }

    @Test
    void testDynamicElementLoadingAndColorChange() {
        LoadingPage loadingPage = new LoadingPage(config.loadingUrl());
        boolean result = loadingPage.isLoaded(loadedColor);
        Assertions.assertTrue(result);
    }
}