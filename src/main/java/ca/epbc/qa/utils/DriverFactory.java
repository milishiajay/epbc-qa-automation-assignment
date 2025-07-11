package ca.epbc.qa.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Factory class for creating and managing WebDriver instances
 */
public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    /**
     * Creates a WebDriver instance based on the specified browser
     * @param browser Browser type (chrome, firefox)
     * @param headless Whether to run in headless mode
     * @return WebDriver instance
     */
    public static WebDriver createDriver(String browser, boolean headless) {
        WebDriver driver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            default:
                logger.warn("Browser '{}' not supported. Defaulting to Chrome.", browser);
                WebDriverManager.chromedriver().setup();
                ChromeOptions defaultOptions = new ChromeOptions();
                if (headless) {
                    defaultOptions.addArguments("--headless");
                }
                defaultOptions.addArguments("--no-sandbox");
                defaultOptions.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(defaultOptions);
        }
        
        // Configure driver timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        
        driverThreadLocal.set(driver);
        logger.info("WebDriver created successfully: {}", browser);
        return driver;
    }
    
    /**
     * Gets the current WebDriver instance for the thread
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    /**
     * Quits the WebDriver and removes it from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            logger.info("WebDriver quit successfully");
        }
    }
}