import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Driver {

    private static Driver DRIVER;

    private static final int DEFAULT_TIMEOUT = 10;

    private WebDriver webDriver;

    private Driver(String browser) {
        JSONObject browsersJson;
        try {
            browsersJson = (JSONObject) new JSONParser().parse(new InputStreamReader(new FileInputStream("browsers.json")));
        } catch (IOException | ParseException e) {
            throw new Error(e);
        }
        switch (browser) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", (String) browsersJson.get(browser));
                webDriver = new ChromeDriver();
                break;
            case "firefox":
                FirefoxOptions options = new FirefoxOptions().setLegacy(true);
                webDriver = new FirefoxDriver(options);
                break;
            default:
                throw new Error("browser is not supported: " + browser);
        }
        webDriver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void init(String browser) {
        if (DRIVER == null) {
            DRIVER = new Driver(browser);
        }
    }

    public static Driver get() {
        if (DRIVER == null) {
            throw new Error("Driver has to be to initialize");
        }
        return DRIVER;
    }

    public void open(String address) {
        webDriver.navigate().to("http://" + address);
    }

    public WebElement find(By selector) {
        return new WebDriverWait(webDriver, DEFAULT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(selector));
    }

    public List<WebElement> findAll(By selector) {
        return new WebDriverWait(webDriver, DEFAULT_TIMEOUT).until(ExpectedConditions.presenceOfAllElementsLocatedBy(selector));
    }

    public void moveToWebElement(WebElement element) {
        Actions action = new Actions(webDriver);
        action.moveToElement(element).build().perform();
    }

    public boolean waitChangeSrc(final WebElement element) {
        final String src = element.getAttribute("src");
        try {
            return new WebDriverWait(webDriver, DEFAULT_TIMEOUT).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return !src.equals(element.getAttribute("src"));
                }
            });
        } catch (TimeoutException err) {
            return false;
        }
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void quit() {
        DRIVER = null;
        webDriver.quit();
    }
}
