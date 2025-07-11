package ca.epbc.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base page class containing common functionality for all page objects
 */
public abstract class BasePage {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Waits for an element to be visible and returns it
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Waits for an element to be clickable and returns it
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Waits for an element to be present in DOM
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement waitForElementPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Clicks on an element after waiting for it to be clickable
     * @param locator Element locator
     */
    protected void clickElement(By locator) {
        WebElement element = waitForElementClickable(locator);
        element.click();
        logger.info("Clicked element: {}", locator);
    }
    
    /**
     * Types text into an element after waiting for it to be visible
     * @param locator Element locator
     * @param text Text to type
     */
    protected void typeText(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
        logger.info("Typed '{}' into element: {}", text, locator);
    }
    
    /**
     * Gets text from an element after waiting for it to be visible
     * @param locator Element locator
     * @return Element text
     */
    protected String getElementText(By locator) {
        WebElement element = waitForElementVisible(locator);
        String text = element.getText();
        logger.info("Got text '{}' from element: {}", text, locator);
        return text;
    }
    
    /**
     * Checks if an element is displayed
     * @param locator Element locator
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets the current page title
     * @return Page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Gets the current page URL
     * @return Page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}