package ru.ivanov.UI.TheInternet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.*;

public class TablesTest {

    @BeforeAll
    static void setup() {
        Configuration.baseUrl = "https://the-internet.herokuapp.com/";
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("xPathProvider")
    void testTableElementsExistAndClickable(By locator, String description, int quantity) {
        open("/tables");
        if (quantity > 1) {
            $$(locator).shouldHave(size(quantity));
            $$(locator).forEach(element ->
                    element.shouldBe(Condition.exist)
                            .shouldBe(Condition.visible)
                            .shouldBe(Condition.clickable)
            );
        } else {
            $(locator)
                    .shouldBe(Condition.exist)
                    .shouldBe(Condition.visible)
                    .shouldBe(Condition.clickable);
        }
    }

    static Stream<Arguments> xPathProvider() {
        return Stream.of(
                Arguments.of(
                        By.xpath("//table[@id='table1']/tbody/tr/td[normalize-space()='Smith']/following-sibling::td[contains(text(), '@')]"),
                        "Email Smith", 1
                ),

                Arguments.of(
                        By.xpath("//table[@id='table1']/tbody/tr/td[contains(text(), '$100.00')]/ancestor::tr//a[normalize-space()='edit']"),
                        "edit со значением $100.00", 1
                ),

                Arguments.of(
                        By.xpath("//table[@id='table1']/tbody/tr/td[starts-with(normalize-space(), 'http://')]"),
                        "Ссылки на http://", 4
                )
        );
    }
}