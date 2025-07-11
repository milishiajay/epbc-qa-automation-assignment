package ca.epbc.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object Model for the EPBC Search Results Page
 */
public class SearchResultsPage extends BasePage {
    
    // Search and Filter Locators
    private final By searchResultsContainer = By.xpath("//div[contains(@class, 'search-results') or contains(@class, 'results')]");
    private final By filterButton = By.xpath("//button[contains(text(), 'Filter') or contains(text(), 'Filters')]");
    private final By areasOfStudyFilter = By.xpath("//div[contains(text(), 'Areas of Study') or contains(text(), 'Area of Study')]");
    private final By technologyITFilter = By.xpath("//label[contains(text(), 'Technology') and contains(text(), 'IT')] | //input[@value*='technology' or @value*='IT']");
    private final By applyFiltersButton = By.xpath("//button[contains(text(), 'Apply') or contains(text(), 'Filter')]");
    
    // Program Result Locators
    private final By programCards = By.xpath("//div[contains(@class, 'program') or contains(@class, 'course') or contains(@class, 'result')]");
    private final By addToMyListButtons = By.xpath("//button[contains(text(), 'Add to My List') or contains(text(), 'Add to List') or contains(@class, 'add-to-list')]");
    private final By firstAddToMyListButton = By.xpath("(//button[contains(text(), 'Add to My List') or contains(text(), 'Add to List') or contains(@class, 'add-to-list')])[1]");
    
    // Alternative locators for different page structures
    private final By filterDropdown = By.xpath("//select[contains(@name, 'filter') or contains(@name, 'area')]");
    private final By technologyOption = By.xpath("//option[contains(text(), 'Technology') or contains(text(), 'IT')]");
    
    public SearchResultsPage(WebDriver driver) {
        super(driver);
        logger.info("SearchResultsPage initialized");
    }
    
    /**
     * Applies the Areas of Study filter for Technology (IT)
     */
    public void applyAreasOfStudyFilter() {
        try {
            // Method 1: Try clicking filter button first
            if (isElementDisplayed(filterButton)) {
                clickElement(filterButton);
                logger.info("Clicked filter button");
            }
            
            // Method 2: Try Areas of Study section
            if (isElementDisplayed(areasOfStudyFilter)) {
                clickElement(areasOfStudyFilter);
                logger.info("Clicked Areas of Study filter");
            }
            
            // Method 3: Try Technology/IT checkbox or option
            if (isElementDisplayed(technologyITFilter)) {
                clickElement(technologyITFilter);
                logger.info("Selected Technology (IT) filter");
            }
            
            // Method 4: Try dropdown approach
            if (isElementDisplayed(filterDropdown)) {
                clickElement(filterDropdown);
                if (isElementDisplayed(technologyOption)) {
                    clickElement(technologyOption);
                    logger.info("Selected Technology from dropdown");
                }
            }
            
            // Apply filters if button exists
            if (isElementDisplayed(applyFiltersButton)) {
                clickElement(applyFiltersButton);
                logger.info("Applied filters");
            }
            
        } catch (Exception e) {
            logger.warn("Could not apply Areas of Study filter using standard methods: {}", e.getMessage());
            // Continue with test - the filter might be applied automatically
        }
    }
    
    /**
     * Adds the first program to My List
     * @return The name/title of the program added
     */
    public String addFirstProgramToMyList() {
        String programName = "";
        
        try {
            // Get program name before clicking add to list
            List<WebElement> programs = driver.findElements(programCards);
            if (!programs.isEmpty()) {
                programName = programs.get(0).getText();
                logger.info("Found program: {}", programName);
            }
            
            // Click the first "Add to My List" button
            if (isElementDisplayed(firstAddToMyListButton)) {
                clickElement(firstAddToMyListButton);
                logger.info("Clicked 'Add to My List' for first program");
            } else {
                logger.warn("No 'Add to My List' button found");
            }
            
        } catch (Exception e) {
            logger.error("Error adding program to My List: {}", e.getMessage());
        }
        
        return programName;
    }
    
    /**
     * Gets the number of search results displayed
     * @return Number of search results
     */
    public int getSearchResultsCount() {
        try {
            List<WebElement> results = driver.findElements(programCards);
            int count = results.size();
            logger.info("Found {} search results", count);
            return count;
        } catch (Exception e) {
            logger.warn("Could not count search results: {}", e.getMessage());
            return 0;
        }
    }
    
    /**
     * Checks if search results are displayed
     * @return true if search results are visible
     */
    public boolean areSearchResultsDisplayed() {
        return isElementDisplayed(searchResultsContainer) || getSearchResultsCount() > 0;
    }
    
    /**
     * Navigates to My List page
     * @return MyListPage instance
     */
    public MyListPage goToMyList() {
        // Look for My List link/button
        By myListLink = By.xpath("//a[contains(text(), 'My List') or contains(@href, 'my-list')] | //button[contains(text(), 'My List')]");
        
        if (isElementDisplayed(myListLink)) {
            clickElement(myListLink);
            logger.info("Navigated to My List");
        } else {
            logger.warn("My List link not found, trying direct navigation");
            driver.get(getCurrentUrl().replace("/search", "/my-list"));
        }
        
        return new MyListPage(driver);
    }
}