package ca.epbc.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object Model for the EPBC Home Page
 */
public class HomePage extends BasePage {
    
    // Locators
    private final By signInButton = By.xpath("//a[contains(@href, '/sign-in') or contains(text(), 'Sign In') or contains(text(), 'Log In')]");
    private final By createAccountButton = By.xpath("//a[contains(@href, '/register') or contains(text(), 'Create Account') or contains(text(), 'Register')]");
    private final By searchBox = By.xpath("//input[@type='search' or @placeholder*='search' or @name*='search']");
    private final By searchButton = By.xpath("//button[@type='submit' or contains(text(), 'Search')]");
    
    public HomePage(WebDriver driver) {
        super(driver);
        logger.info("HomePage initialized");
    }
    
    /**
     * Navigates to the EPBC staging homepage
     * @param url The URL to navigate to
     */
    public void navigateToHomePage(String url) {
        driver.get(url);
        logger.info("Navigated to homepage: {}", url);
    }
    
    /**
     * Clicks on the Sign In button
     * @return SignInPage instance
     */
    public SignInPage clickSignIn() {
        clickElement(signInButton);
        logger.info("Clicked Sign In button");
        return new SignInPage(driver);
    }
    
    /**
     * Clicks on the Create Account button
     * @return SignInPage instance (assuming it goes to same page with registration form)
     */
    public SignInPage clickCreateAccount() {
        clickElement(createAccountButton);
        logger.info("Clicked Create Account button");
        return new SignInPage(driver);
    }
    
    /**
     * Performs a search from the homepage
     * @param searchTerm The term to search for
     * @return SearchResultsPage instance
     */
    public SearchResultsPage performSearch(String searchTerm) {
        typeText(searchBox, searchTerm);
        clickElement(searchButton);
        logger.info("Performed search for: {}", searchTerm);
        return new SearchResultsPage(driver);
    }
    
    /**
     * Checks if the homepage is loaded
     * @return true if homepage is loaded
     */
    public boolean isHomePageLoaded() {
        return getCurrentUrl().contains("educationplannerbc.ca");
    }
}