package yandex.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;

import java.util.List;

import static org.junit.Assert.fail;

public class YandexTest {

    String imageURL = "https://xcmg-ru.ru/upload/iblock/ca1/ca15a63ec4d8361368ee13192246d563.png";

    static {
        System.setProperty("webdriver.chrome.driver", "src/test/testResources/chromedriver.exe");
    }

    private WebDriver driver;

    @Before
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
        driver = new ChromeDriver(chromeOptions);
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void test() throws InterruptedException {
        try {
            driver.get("https://yandex.ru");
            Thread.sleep(5000);

            driver.findElement(By.linkText("Картинки")).click();
            Thread.sleep(5000);

            List<WebElement> search2__input = driver.findElements(By.className("search2__input"));
            for (WebElement webElement : search2__input) {
                System.out.println(webElement.getText());
            }

            List<WebElement> elements = driver.findElements(By.className("input__cbir-button input__button"));
            elements.get(0).findElement(By.tagName("button")).click();
            Thread.sleep(5000);
            WebElement element = driver.findElement(By.cssSelector("body > div.cbir-panel.i-bem.cbir-panel_dragdrop_yes.cbir-panel_js_inited.cbir-panel_visibility_visible > div > div.cbir-panel__tabs > div > form.cbir-panel__input > span"));
            element.sendKeys(imageURL);
            Thread.sleep(5000);
            driver.findElement(By.name("cbir-submit")).click();
            Thread.sleep(5000);

            System.out.println("test");
        } finally {
            driver.close();
        }



//        List<WebElement> elements = driver.findElements(By.tagName("a"));
//        for(WebElement element : elements){
//            if(element.getAttribute("data-id").equals("images")){
//                element.click();
//                break;
//            }
//        }
//        Thread.sleep(3);
//    }
}}
