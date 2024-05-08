// load JAR-file: "java -jar artifacts/app-replan-delivery.jar" before starting test

/* Поиск по селектору:
    1. По классу - ".название класса" (.name)
    2. По атрибуту - "[атрибут]" ([name=first_name])
    3. По id - "'#название'" ('#name')
    4. По тегу - "input" ($ один или $$ несколько)

    ***
    Simple селекторы можно выстраивать в последовательность, требуя соответствия всем селекторам, входящим в последовательность, например, input[type="search"] раскладывается на два simple:

    input (селектор по тегу)
    [type="search"] (селектор по значению атрибута)
* */
//import DataGenerator;
import com.codeborne.selenide.*;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.LocalDate.now;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

class CallbackSelenideTest {

    private Faker faker;
    @BeforeEach
    void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }


    public String generateDate(int days) {
        return now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
    @Test
    void shouldTest() throws InterruptedException {

        // City
        String city = String.valueOf(faker.address().city());
        // First date
        String firstDate = generateDate(4);
        // Second date
        String secondDate = generateDate(5);
        // Full name
        String fullName = String.valueOf(faker.name().fullName());
        // Phone number
        String phoneNumber = String.valueOf(faker.phoneNumber().phoneNumber());

        open("http://localhost:9999");

        SelenideElement form = $("form");
        ElementsCollection button = $$("button");

        // Первое заполнение анкеты
        form.find("[data-test-id=city] input").setValue(city);
        form.find("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        form.find("[data-test-id=date] input").setValue(firstDate);
        form.find("[data-test-id=name] input").setValue(fullName);
        form.find("[data-test-id=phone] input").setValue(phoneNumber);
        form.find("[data-test-id='agreement']").click();
        button.get(1).click();
        $(".notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstDate));
        button.get(1).click();
        button.get(3).click();
        $(".notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstDate));

        //sleep(20000);
    }
}