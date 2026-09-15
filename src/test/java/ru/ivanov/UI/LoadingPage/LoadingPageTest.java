package ru.ivanov.UI.LoadingPage;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.ivanov.UI.LoadingPage.ColorCondition.textColorIs;

public class LoadingPageTest {

    @BeforeAll
    static void setup() {
        Configuration.baseUrl = "https://the-internet.herokuapp.com/";
    }

    @Test
    void testDynamicElementLoadingAndColorChange() {
        open("/dynamic_loading/2");
        $("[id = 'start'] button").click();
        var finishText = $("[id = 'finish'] h4");
        finishText.shouldBe(visible, Duration.ofSeconds(10));
        finishText.should(textColorIs("rgba(34, 34, 34, 1)"), Duration.ofSeconds(10));
    }
}