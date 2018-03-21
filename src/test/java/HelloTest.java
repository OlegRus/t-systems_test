import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class HelloTest {

    private Driver driver;
    private Yandex yandex;

    @Parameters("browser")
    @BeforeTest
    public void setUp(@Optional("firefox")String browser) {
        Driver.init(browser);
        driver = Driver.get();
        yandex = new Yandex(driver);
        yandex.openMainPage();
        yandex.search("котята");
        yandex.goToTab("Видео");
    }

    @DataProvider
    public Object[][] kittensSet() {
        List<WebElement> previewsList = yandex.getPreviewImages();
        Object[][] data = new Object[previewsList.size()][1];
        for (int i = 0; i < previewsList.size(); i++) {
            WebElement kitten = previewsList.get(i);
            data[i] = new Object[] {kitten};
        }
        return data;
    }

    @Test(dataProvider = "kittensSet")
    public void checkKittens(WebElement kitten) {
        driver.moveToWebElement(kitten);
        String searchQuery = yandex.getSearchQueryForKitten(kitten);
        Assert.assertTrue(driver.waitChangeSrc(kitten), String.format("Image doesn't change\nSearch query: '%s'", searchQuery));
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
