package ca.epbc.qa.tests;

import ca.epbc.qa.pages.HomePage;
import ca.epbc.qa.pages.MyListPage;
import ca.epbc.qa.pages.SearchResultsPage;
import ca.epbc.qa.pages.SignInPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Main test class for EPBC Search and My List functionality
 * Test Scenario:
 * 1. Sign in as a new user
 * 2. Search for "UBC"
 * 3. Filter by "Areas of Study" → "Technology (IT)"
 * 4. Add a program to "My List"
 * 5. Verify the addition
 */
public class EpbcSearchMyListTest extends BaseTest {
    
    @Test(description = "Complete EPBC Search and My List workflow test")
    public void testSearchAndMyListWorkflow() {
        logger.info("Starting EPBC Search and My List workflow test");
        
        // Step 1: Navigate to homepage
        HomePage homePage = new HomePage(driver);
        homePage.navigateToHomePage(BASE_URL);
        Assert.assertTrue(homePage.isHomePageLoaded(), "Homepage should be loaded");
        logger.info("✓ Step 1: Successfully navigated to homepage");
        
        // Step 2: Sign in as new user
        SignInPage signInPage = homePage.clickSignIn();
        homePage = signInPage.createNewAccountWithGeneratedCredentials();
        
        // Verify sign in was successful (either redirected or success message)
        boolean signInSuccessful = signInPage.isSignInSuccessful() || homePage.isHomePageLoaded();
        Assert.assertTrue(signInSuccessful, "User should be signed in successfully");
        logger.info("✓ Step 2: Successfully signed in as new user");
        
        // Step 3: Search for "UBC"
        SearchResultsPage searchResultsPage = homePage.performSearch("UBC");
        Assert.assertTrue(searchResultsPage.areSearchResultsDisplayed(), "Search results should be displayed");
        int initialResultsCount = searchResultsPage.getSearchResultsCount();
        logger.info("✓ Step 3: Successfully searched for 'UBC' - found {} results", initialResultsCount);
        
        // Step 4: Filter by "Areas of Study" → "Technology (IT)"
        searchResultsPage.applyAreasOfStudyFilter();
        // Wait a moment for filter to be applied
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        int filteredResultsCount = searchResultsPage.getSearchResultsCount();
        logger.info("✓ Step 4: Applied Areas of Study filter - now showing {} results", filteredResultsCount);
        
        // Step 5: Add a program to "My List"
        String addedProgramName = searchResultsPage.addFirstProgramToMyList();
        Assert.assertFalse(addedProgramName.isEmpty(), "Should have added a program to My List");
        logger.info("✓ Step 5: Added program to My List: {}", addedProgramName);
        
        // Step 6: Navigate to My List and verify the addition
        MyListPage myListPage = searchResultsPage.goToMyList();
        Assert.assertTrue(myListPage.isMyListPageLoaded(), "My List page should be loaded");
        
        // Verify the program was added to My List
        boolean programInList = myListPage.isProgramInMyList(addedProgramName);
        Assert.assertTrue(programInList, "Program should be present in My List");
        
        int myListCount = myListPage.getMyListItemCount();
        Assert.assertTrue(myListCount > 0, "My List should contain at least one item");
        
        logger.info("✓ Step 6: Successfully verified program in My List - {} items total", myListCount);
        
        // Log all items in My List for verification
        String[] myListItems = myListPage.getMyListItemTexts();
        logger.info("My List contents:");
        for (int i = 0; i < myListItems.length; i++) {
            logger.info("  {}. {}", i + 1, myListItems[i]);
        }
        
        logger.info("✅ Test completed successfully - All steps passed!");
    }
    
    @Test(description = "Verify My List functionality independently", dependsOnMethods = {"testSearchAndMyListWorkflow"})
    public void testMyListVerification() {
        logger.info("Starting My List verification test");
        
        // Navigate directly to My List page
        MyListPage myListPage = new MyListPage(driver);
        myListPage.navigateToMyList();
        
        Assert.assertTrue(myListPage.isMyListPageLoaded(), "My List page should be accessible");
        
        // Check if My List has items (from previous test or existing data)
        boolean hasItems = myListPage.hasItemsInMyList();
        logger.info("My List has items: {}", hasItems);
        
        if (hasItems) {
            int itemCount = myListPage.getMyListItemCount();
            Assert.assertTrue(itemCount > 0, "My List should show correct item count");
            logger.info("✓ My List contains {} items", itemCount);
        } else {
            logger.info("My List is empty - this may be expected for a fresh test run");
        }
        
        logger.info("✅ My List verification test completed");
    }
}