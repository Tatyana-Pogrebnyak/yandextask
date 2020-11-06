package yandex.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class YandexTest {

    static final String CRANE_IMAGE_URL = "https://xcmg-ru.ru/upload/iblock/ca1/ca15a63ec4d8361368ee13192246d563.png";
    static final String MAIN_PAGE_URL = "https://yandex.ru";

    static final By PICTURES_XPATH = By.xpath("/html/body/div[1]/div[2]/div[3]/div/div[2]/div/div/div[2]/a[2]/div[1]");
    static final By ADD_PICTURE_BUTTON_XPATH = By.xpath("html/body/header/div/div[2]/div[1]/form/div[1]/span/span/div[2]/button");
    static final By SEARCH_PANEL_XPATH = By.xpath("/html/body/div[2]/div/div[1]/div/form[2]/span/span");
    static final By FIND_PICTURE_BUTTON_XPATH = By.xpath("/html/body/div[2]/div/div[1]/div/form[2]/button");
    static final By PICTURE_ASSUMPTIONS_PANEL = By.xpath("/html/body/div[5]/div[1]/div[1]/div[1]/div[2]");

    static {
        System.setProperty("webdriver.chrome.driver", "src/test/testResources/chromedriver.exe");
    }

    private WebDriver driver;
    private PictureToFind pictureToFind;

    @Before
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
        driver = new ChromeDriver(chromeOptions);

        pictureToFind = new PictureToFind(CRANE_IMAGE_URL, "кран");
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void test() throws InterruptedException {
        //load main yandex page
        driver.get(MAIN_PAGE_URL);
        waitPageLoaded();

        //click to pictures item and navigate to newly opened tab
        driver.findElement(PICTURES_XPATH).click();
        navigateToTab(1);

        //click on find by picture button
        driver.findElement(ADD_PICTURE_BUTTON_XPATH).click();

        //set picture URL to find
        WebElement searchPanel = driver.findElement(SEARCH_PANEL_XPATH);
        searchPanel.findElement(By.tagName("input")).sendKeys(pictureToFind.URL);

        //wait a little bit while search picture button will enabled
        Thread.sleep(200);

        driver.findElement(FIND_PICTURE_BUTTON_XPATH).click();

        //wait until picture will by found by yandex
        Thread.sleep(4000);
        List<String> results = findAssumptionsForPicture();

        assertTrue("No assumptions for the picture found", results.size() > 0);
        System.out.println("Found assumptions are: " + results);
    }

    private void waitPageLoaded() {
        new WebDriverWait(driver, 1).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    private void navigateToTab(int tabNumber) {
        List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowHandles.get(tabNumber));
    }

    private List<String> findAssumptionsForPicture() {
        List<WebElement> assumptions = driver.findElements(PICTURE_ASSUMPTIONS_PANEL).get(0)
                .findElements(By.tagName("span"));

        List<String> results = new ArrayList<>();
        String spanValue;
        for (WebElement spanElement : assumptions) {
            spanValue = spanElement.getText();
            if (spanValue != null && spanValue.trim().length() != 0) {
                if (spanValue.contains(pictureToFind.assumptionKeyWord)) {
                    results.add(spanValue);
                }
            }
        }

        return results;
    }

    private class PictureToFind {
        String URL;
        String assumptionKeyWord;

        public PictureToFind(String URL, String assumptionKeyWord) {
            this.URL = URL;
            this.assumptionKeyWord = assumptionKeyWord;
        }
    }
}
