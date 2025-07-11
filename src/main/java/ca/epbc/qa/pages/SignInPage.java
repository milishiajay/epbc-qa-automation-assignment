package ca.epbc.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object Model for the EPBC Sign In/Registration Page
 */
public class SignInPage extends BasePage {
    
    // Sign In Locators
    private final By emailField = By.xpath("//input[@type='email' or @name='email' or @id='email']");
    private final By passwordField = By.xpath("//input[@type='password' or @name='password' or @id='password']");
    private final By signInButton = By.xpath("//button[@type='submit' or contains(text(), 'Sign In') or contains(text(), 'Log In')]");
    
    // Registration Locators
    private final By createAccountTab = By.xpath("//a[contains(text(), 'Create Account') or contains(text(), 'Register')]");
    private final By firstNameField = By.xpath("//input[@name='firstName' or @id='firstName' or @placeholder*='First']");
    private final By lastNameField = By.xpath("//input[@name='lastName' or @id='lastName' or @placeholder*='Last']");
    private final By registerEmailField = By.xpath("//input[@type='email' and (@name='email' or @id='email')]");
    private final By registerPasswordField = By.xpath("//input[@type='password' and (@name='password' or @id='password')]");
    private final By confirmPasswordField = By.xpath("//input[@type='password' and (@name='confirmPassword' or @id='confirmPassword' or @placeholder*='Confirm')]");
    private final By createAccountButton = By.xpath("//button[@type='submit' or contains(text(), 'Create Account') or contains(text(), 'Register')]");
    private final By termsCheckbox = By.xpath("//input[@type='checkbox']");
    
    // Success/Error Messages
    private final By successMessage = By.xpath("//div[contains(@class, 'success') or contains(@class, 'alert-success')]");
    private final By errorMessage = By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert-error')]");
    
    public SignInPage(WebDriver driver) {
        super(driver);
        logger.info("SignInPage initialized");
    }
    
    /**
     * Signs in with existing credentials
     * @param email User email
     * @param password User password
     * @return HomePage instance after successful sign in
     */
    public HomePage signInWithCredentials(String email, String password) {
        typeText(emailField, email);
        typeText(passwordField, password);
        clickElement(signInButton);
        logger.info("Attempted sign in with email: {}", email);
        return new HomePage(driver);
    }
    
    /**
     * Creates a new user account
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email User's email
     * @param password User's password
     * @return HomePage instance after successful registration
     */
    public HomePage createNewAccount(String firstName, String lastName, String email, String password) {
        // Click create account tab if present
        if (isElementDisplayed(createAccountTab)) {
            clickElement(createAccountTab);
        }
        
        // Fill registration form
        if (isElementDisplayed(firstNameField)) {
            typeText(firstNameField, firstName);
        }
        if (isElementDisplayed(lastNameField)) {
            typeText(lastNameField, lastName);
        }
        
        typeText(registerEmailField, email);
        typeText(registerPasswordField, password);
        
        if (isElementDisplayed(confirmPasswordField)) {
            typeText(confirmPasswordField, password);
        }
        
        // Accept terms if checkbox is present
        if (isElementDisplayed(termsCheckbox)) {
            clickElement(termsCheckbox);
        }
        
        clickElement(createAccountButton);
        logger.info("Attempted to create new account with email: {}", email);
        return new HomePage(driver);
    }
    
    /**
     * Creates a new account with generated credentials
     * @return HomePage instance after successful registration
     */
    public HomePage createNewAccountWithGeneratedCredentials() {
        long timestamp = System.currentTimeMillis();
        String email = "testuser" + timestamp + "@example.com";
        String password = "TestPassword123!";
        String firstName = "Test";
        String lastName = "User";
        
        return createNewAccount(firstName, lastName, email, password);
    }
    
    /**
     * Checks if sign in was successful
     * @return true if sign in was successful
     */
    public boolean isSignInSuccessful() {
        // Check if we're redirected away from sign in page or if success message is shown
        return !getCurrentUrl().contains("sign-in") || isElementDisplayed(successMessage);
    }
    
    /**
     * Checks if there's an error message displayed
     * @return true if error message is displayed
     */
    public boolean isErrorDisplayed() {
        return isElementDisplayed(errorMessage);
    }
    
    /**
     * Gets the error message text
     * @return Error message text
     */
    public String getErrorMessage() {
        if (isErrorDisplayed()) {
            return getElementText(errorMessage);
        }
        return "";
    }
}