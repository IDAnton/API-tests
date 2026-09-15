package ru.ivanov.UI.LoadingPage;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.openqa.selenium.WebElement;

public class ColorCondition {
    public static WebElementCondition textColorIs(String expectedRgb) {
        return new WebElementCondition("textColorIs") {
            @Override
            public @NonNull CheckResult check(Driver driver, WebElement webElement) {
                String actualColor = webElement.getCssValue("color");
                if (actualColor.equals(expectedRgb)) {
                    return CheckResult.accepted(actualColor);
                } else {
                    return CheckResult.rejected("wrong text color", actualColor);
                }
            }
        };
    }
}