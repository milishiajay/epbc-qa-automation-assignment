# EPBC Automation - Troubleshooting Guide

## Common Issues and Solutions

### 1. WebDriver Issues

#### Issue: Chrome Driver Version Mismatch
**Symptoms:**
```
SessionNotCreatedException: This version of ChromeDriver only supports Chrome version X
```

**Solutions:**
```bash
# Option 1: Update Chrome browser
sudo apt update && sudo apt upgrade google-chrome-stable  # Linux
# Or download latest Chrome from google.com/chrome

# Option 2: Clear WebDriver cache
rm -rf ~/.cache/selenium/
mvn test -Dheadless=true

# Option 3: Force specific ChromeDriver version
mvn test -Dwebdriver.chrome.driver=/path/to/chromedriver
```

#### Issue: Chrome User Data Directory Conflict
**Symptoms:**
```
SessionNotCreatedException: user data directory is already in use
```

**Solutions:**
```bash
# Kill existing Chrome processes
pkill -f chrome
pkill -f chromium

# Or use unique user data directory
mvn test -Dheadless=true -Dchrome.userDataDir=/tmp/chrome-$(date +%s)
```

#### Issue: WebDriver Not Found
**Symptoms:**
```
WebDriverException: 'chromedriver' executable needs to be in PATH
```

**Solutions:**
```bash
# Verify WebDriverManager is working
mvn dependency:tree | grep webdrivermanager

# Manual driver setup (if needed)
wget https://chromedriver.storage.googleapis.com/LATEST_RELEASE
export PATH=$PATH:/path/to/chromedriver/directory
```

### 2. Element Location Issues

#### Issue: Element Not Found
**Symptoms:**
```
NoSuchElementException: Unable to locate element
TimeoutException: Expected condition failed
```

**Debugging Steps:**
```bash
# Run in non-headless mode to see the page
mvn test -Dheadless=false

# Add debug logging
mvn test -X -Dheadless=false

# Take screenshot on failure (add to test code)
((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
```

**Solutions:**
```java
// 1. Increase wait time
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

// 2. Use multiple locator strategies
List<By> locators = Arrays.asList(
    By.id("email"),
    By.name("email"),
    By.xpath("//input[@type='email']"),
    By.cssSelector("input[type='email']")
);

// 3. Wait for page to be ready
wait.until(webDriver -> ((JavascriptExecutor) webDriver)
    .executeScript("return document.readyState").equals("complete"));
```

#### Issue: Element Not Clickable
**Symptoms:**
```
ElementClickInterceptedException: Element is not clickable
```

**Solutions:**
```java
// 1. Wait for element to be clickable
wait.until(ExpectedConditions.elementToBeClickable(element));

// 2. Scroll element into view
((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

// 3. Use JavaScript click as fallback
((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
```

### 3. Test Execution Issues

#### Issue: Tests Not Running
**Symptoms:**
```
No tests were executed
Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
```

**Solutions:**
```bash
# Check TestNG configuration
cat src/test/resources/testng.xml

# Verify test class naming convention
ls src/test/java/**/*Test.java

# Run specific test
mvn test -Dtest=EpbcSearchMyListTest

# Check for compilation errors
mvn clean compile test-compile
```

#### Issue: Maven Build Failures
**Symptoms:**
```
BUILD FAILURE
Failed to execute goal
```

**Solutions:**
```bash
# Clean and rebuild
mvn clean install

# Check Java version
java -version  # Should be 11+

# Verify Maven version
mvn -version   # Should be 3.6+

# Check dependencies
mvn dependency:resolve
```

### 4. Network and Connectivity Issues

#### Issue: Website Not Accessible
**Symptoms:**
```
UnreachableHostException: Could not reach host
TimeoutException: Page load timeout
```

**Solutions:**
```bash
# Test connectivity
curl -I https://stg-www.educationplannerbc.ca

# Check DNS resolution
nslookup stg-www.educationplannerbc.ca

# Use different base URL
mvn test -DbaseUrl=https://www.educationplannerbc.ca
```

#### Issue: Slow Page Loading
**Symptoms:**
```
TimeoutException: Page load timeout after 30 seconds
```

**Solutions:**
```java
// Increase page load timeout
driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

// Use custom wait conditions
wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
```

### 5. Memory and Performance Issues

#### Issue: Out of Memory Errors
**Symptoms:**
```
OutOfMemoryError: Java heap space
OutOfMemoryError: Metaspace
```

**Solutions:**
```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx2048m -XX:MaxMetaspaceSize=512m"

# Run with memory settings
mvn test -Dheadless=true -Xmx2048m

# Clean up between tests
mvn clean test
```

#### Issue: Slow Test Execution
**Symptoms:**
- Tests taking longer than expected
- Browser hanging or freezing

**Solutions:**
```bash
# Use headless mode
mvn test -Dheadless=true

# Reduce implicit wait times
# (modify in DriverFactory.java)

# Run tests in parallel
mvn test -Dparallel=methods -DthreadCount=2
```

### 6. Environment-Specific Issues

#### Issue: Linux Display Issues
**Symptoms:**
```
WebDriverException: unknown error: cannot find Chrome binary
```

**Solutions:**
```bash
# Install Chrome on Linux
wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
sudo apt update
sudo apt install google-chrome-stable

# Use headless mode
mvn test -Dheadless=true

# Set display variable (if needed)
export DISPLAY=:99
```

#### Issue: Windows Path Issues
**Symptoms:**
```
WebDriverException: CreateProcess error=2
```

**Solutions:**
```cmd
# Add Chrome to PATH
set PATH=%PATH%;C:\Program Files\Google\Chrome\Application

# Use full path in test configuration
-Dwebdriver.chrome.driver=C:\path\to\chromedriver.exe
```

### 7. TestNG Configuration Issues

#### Issue: TestNG XML Parsing Error
**Symptoms:**
```
SAXParseException: The processing instruction target matching "[xX][mM][lL]" is not allowed
```

**Solutions:**
```xml
<!-- Remove any blank lines before XML declaration -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="EPBC Automation Test Suite">
    <!-- suite content -->
</suite>
```

#### Issue: Test Dependencies Not Working
**Symptoms:**
```
Tests running in wrong order
Dependent tests failing
```

**Solutions:**
```java
// Use proper TestNG annotations
@Test(dependsOnMethods = "testSetup")
public void testMainWorkflow() {
    // test code
}

// Or use groups
@Test(groups = "setup")
@Test(groups = "main", dependsOnGroups = "setup")
```

### 8. Debugging Techniques

#### Enable Verbose Logging
```bash
# Maven debug mode
mvn test -X -Dheadless=false

# TestNG verbose output
mvn test -Dtestng.verbose=2

# Custom logging level
mvn test -Dlog4j.configuration=debug
```

#### Screenshot on Failure
```java
// Add to test teardown
@AfterMethod
public void takeScreenshotOnFailure(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // Save screenshot with timestamp
    }
}
```

#### Browser Developer Tools
```java
// Enable Chrome DevTools
ChromeOptions options = new ChromeOptions();
options.addArguments("--remote-debugging-port=9222");

// Access at: http://localhost:9222
```

### 9. CI/CD Specific Issues

#### Issue: Tests Pass Locally but Fail in CI
**Common Causes:**
- Different browser versions
- Missing display server
- Network restrictions
- Resource limitations

**Solutions:**
```yaml
# GitHub Actions example
- name: Setup Chrome
  uses: browser-actions/setup-chrome@latest
  
- name: Run tests
  run: mvn test -Dheadless=true
  env:
    DISPLAY: :99
```

#### Issue: Flaky Tests in CI
**Solutions:**
```java
// Add retry logic
@Retryable(value = {Exception.class}, maxAttempts = 3)

// Increase timeouts for CI
if (System.getenv("CI") != null) {
    timeout = timeout * 2;
}
```

### 10. Quick Diagnostic Commands

```bash
# System check
java -version && mvn -version && google-chrome --version

# Project validation
mvn clean compile test-compile

# Dependency check
mvn dependency:tree | grep -E "(selenium|testng|webdriver)"

# Quick test run
mvn test -Dtest=EpbcSearchMyListTest -Dheadless=true

# Full clean test
mvn clean test -Dheadless=true

# Generate reports
mvn surefire-report:report
```

### 11. Getting Help

#### Log Analysis
1. Check console output for error patterns
2. Review Surefire reports in `target/surefire-reports/`
3. Look for stack traces and root causes
4. Check browser logs if available

#### Community Resources
- [Selenium Documentation](https://selenium-python.readthedocs.io/)
- [TestNG Documentation](https://testng.org/doc/)
- [WebDriverManager GitHub](https://github.com/bonigarcia/webdrivermanager)
- [Stack Overflow Selenium Tag](https://stackoverflow.com/questions/tagged/selenium)

#### Professional Support
- Review test execution logs
- Provide system information
- Include error messages and stack traces
- Share test configuration details

---

*This troubleshooting guide covers the most common issues encountered in Selenium WebDriver automation. For specific issues not covered here, check the project logs and consider the debugging techniques provided.*