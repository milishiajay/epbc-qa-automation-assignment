package ca.epbc.qa.tests;

import ca.epbc.qa.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

/**
 * Base test class containing common setup and teardown methods
 */
public class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected WebDriver driver;
    protected static final String BASE_URL = "https://stg-www.educationplannerbc.ca";
    
    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp(String browser, String headless) {
        logger.info("Setting up test with browser: {} (headless: {})", browser, headless);
        boolean isHeadless = Boolean.parseBoolean(headless);
        driver = DriverFactory.createDriver(browser, isHeadless);
        logger.info("Test setup completed successfully");
    }
    
    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down test");
        DriverFactory.quitDriver();
        logger.info("Test teardown completed");
    }
}