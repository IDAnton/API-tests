package ru.ivanov.UI.LoadingPage;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.ivanov.UI.LoadingPage.ColorCondition.textColorIs;

public class LoadingPage {
    private final SelenideElement loadingButton = $("[id = 'start'] button");
    private final SelenideElement finishText = $("[id = 'finish'] h4");

    LoadingPage(String url) {
        open(url);
        loadingButton.click();
    }

    public boolean isLoaded(String loadedColor) {
        finishText.shouldBe(visible, Duration.ofSeconds(10));
        finishText.should(textColorIs(loadedColor), Duration.ofSeconds(10));
        return finishText.isDisplayed();
    }
}
