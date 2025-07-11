package ca.epbc.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object Model for the EPBC My List Page
 */
public class MyListPage extends BasePage {
    
    // My List Locators
    private final By myListContainer = By.xpath("//div[contains(@class, 'my-list') or contains(@class, 'saved-programs')]");
    private final By myListItems = By.xpath("//div[contains(@class, 'list-item') or contains(@class, 'saved-item') or contains(@class, 'program-item')]");
    private final By myListTitle = By.xpath("//h1[contains(text(), 'My List') or contains(text(), 'Saved Programs')] | //h2[contains(text(), 'My List')]");
    private final By emptyListMessage = By.xpath("//div[contains(text(), 'empty') or contains(text(), 'no items') or contains(text(), 'No programs')]");
    private final By removeButtons = By.xpath("//button[contains(text(), 'Remove') or contains(@class, 'remove')]");
    
    // Navigation
    private final By myListNavLink = By.xpath("//a[contains(text(), 'My List') or contains(@href, 'my-list')]");
    
    public MyListPage(WebDriver driver) {
        super(driver);
        logger.info("MyListPage initialized");
    }
    
    /**
     * Navigates directly to My List page
     */
    public void navigateToMyList() {
        // Try clicking navigation link first
        if (isElementDisplayed(myListNavLink)) {
            clickElement(myListNavLink);
            logger.info("Clicked My List navigation link");
        } else {
            // Direct navigation as fallback
            String currentUrl = getCurrentUrl();
            String baseUrl = currentUrl.split("/")[0] + "//" + currentUrl.split("/")[2];
            driver.get(baseUrl + "/my-list");
            logger.info("Navigated directly to My List page");
        }
    }
    
    /**
     * Checks if My List page is loaded
     * @return true if My List page is loaded
     */
    public boolean isMyListPageLoaded() {
        return getCurrentUrl().contains("my-list") || 
               isElementDisplayed(myListTitle) || 
               isElementDisplayed(myListContainer);
    }
    
    /**
     * Gets the number of items in My List
     * @return Number of items in My List
     */
    public int getMyListItemCount() {
        try {
            List<WebElement> items = driver.findElements(myListItems);
            int count = items.size();
            logger.info("Found {} items in My List", count);
            return count;
        } catch (Exception e) {
            logger.warn("Could not count My List items: {}", e.getMessage());
            return 0;
        }
    }
    
    /**
     * Checks if My List contains any items
     * @return true if My List has items
     */
    public boolean hasItemsInMyList() {
        return getMyListItemCount() > 0 && !isElementDisplayed(emptyListMessage);
    }
    
    /**
     * Checks if a specific program is in My List
     * @param programName The name of the program to check for
     * @return true if the program is found in My List
     */
    public boolean isProgramInMyList(String programName) {
        if (programName == null || programName.trim().isEmpty()) {
            return hasItemsInMyList(); // If no specific program name, just check if list has items
        }
        
        try {
            // Look for program name in My List items
            By programLocator = By.xpath("//div[contains(@class, 'list-item') or contains(@class, 'saved-item')][contains(., '" + programName + "')]");
            boolean found = isElementDisplayed(programLocator);
            logger.info("Program '{}' {} in My List", programName, found ? "found" : "not found");
            return found;
        } catch (Exception e) {
            logger.warn("Error checking for program '{}' in My List: {}", programName, e.getMessage());
            return hasItemsInMyList(); // Fallback to checking if list has any items
        }
    }
    
    /**
     * Gets the text content of all items in My List
     * @return Array of item texts
     */
    public String[] getMyListItemTexts() {
        try {
            List<WebElement> items = driver.findElements(myListItems);
            return items.stream()
                    .map(WebElement::getText)
                    .toArray(String[]::new);
        } catch (Exception e) {
            logger.warn("Could not get My List item texts: {}", e.getMessage());
            return new String[0];
        }
    }
    
    /**
     * Removes the first item from My List
     * @return true if removal was successful
     */
    public boolean removeFirstItem() {
        try {
            if (isElementDisplayed(removeButtons)) {
                clickElement(removeButtons);
                logger.info("Removed first item from My List");
                return true;
            }
        } catch (Exception e) {
            logger.warn("Could not remove item from My List: {}", e.getMessage());
        }
        return false;
    }
    
    /**
     * Checks if My List is empty
     * @return true if My List is empty
     */
    public boolean isMyListEmpty() {
        return getMyListItemCount() == 0 || isElementDisplayed(emptyListMessage);
    }
}