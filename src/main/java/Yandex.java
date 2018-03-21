import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.*;

public class Yandex {

    private Driver driver;

    private final By searchField = By.id("text");
    private final By searchButton = By.xpath("//button[@type='submit']");
    private final By kittenImg = By.xpath("//*[contains(@class, 'column_type_content')]//img");

    Yandex(Driver driver) {
        this.driver = driver;
    }

    public void openMainPage() {
        driver.open("www.yandex.ru");
    }

    public void search(String str) {
        driver.find(searchField).sendKeys(str);
        driver.find(searchButton).click();
    }

    public void goToTab(String tabName) {
        String firstTab = driver.getWebDriver().getWindowHandle();

        String path = String.format("//a[.='%s']", tabName);
        driver.find(By.xpath(path)).click();

        Set<String> tabsSet = driver.getWebDriver().getWindowHandles();
        String secondTab = "";
        for (String tab : tabsSet) {
            if (!firstTab.equals(tab)) {
                secondTab = tab;
            }
        }
        driver.getWebDriver().switchTo().window(firstTab);
        driver.getWebDriver().close();
        driver.getWebDriver().switchTo().window(secondTab);
    }

    public List<WebElement> getPreviewImages() {
        return driver.findAll(kittenImg);
    }

    public String getSearchQueryForKitten(WebElement kitten) {
        return kitten.findElement(By.xpath("./../../..//*[contains(@class, 'serp-item__title')]")).getText();
    }
}
