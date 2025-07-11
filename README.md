This task was completed by DeepAgent with the prompt 'complete this' referring to the uploaded technical assignment document. The task was completed efficiently using AI automation.

# EPBC QA Automation Assignment

## Project Overview

This is a comprehensive Selenium 4 Java automation testing project for the Education Planner BC (EPBC) website. The project implements automated testing for the search functionality and "My List" feature using the Page Object Model design pattern.

## Project Structure

```
epbc-automation-assignment/
├── README.md                           # Project documentation
├── EXECUTION_GUIDE.md                  # Detailed execution instructions
├── TEST_REPORT.md                      # Test execution report and findings
├── TROUBLESHOOTING.md                  # Common issues and solutions
├── pom.xml                            # Maven configuration
├── reports/                           # Test execution reports
│   └── console.txt                    # Console output from test runs
├── src/
│   ├── main/java/ca/epbc/qa/
│   │   ├── pages/                     # Page Object Model classes
│   │   │   ├── BasePage.java          # Base page with common functionality
│   │   │   ├── HomePage.java          # Homepage page object
│   │   │   ├── SearchPage.java        # Search page object
│   │   │   ├── SignInPage.java        # Sign-in page object
│   │   │   └── MyListPage.java        # My List page object
│   │   └── utils/                     # Utility classes
│   │       ├── DriverFactory.java     # WebDriver management
│   │       └── TestDataGenerator.java # Test data generation
│   └── test/
│       ├── java/ca/epbc/qa/tests/
│       │   ├── BaseTest.java          # Base test class
│       │   └── EpbcSearchMyListTest.java # Main test class
│       └── resources/
│           └── testng.xml             # TestNG configuration
└── target/                            # Maven build artifacts
    └── surefire-reports/              # Test execution reports
```

## Technology Stack

- **Java 11**: Programming language
- **Selenium WebDriver 4.15.0**: Web automation framework
- **TestNG 7.8.0**: Testing framework
- **Maven 3.x**: Build and dependency management
- **WebDriverManager 5.6.2**: Automatic driver management
- **Chrome/ChromeDriver**: Browser automation
- **Page Object Model**: Design pattern for maintainable test code

## Key Features

### 1. Page Object Model Implementation
- **BasePage**: Common functionality for all page objects
- **Specific Page Objects**: HomePage, SearchPage, SignInPage, MyListPage
- **Encapsulation**: Each page object encapsulates its elements and actions

### 2. Robust WebDriver Management
- **DriverFactory**: Centralized WebDriver creation and configuration
- **Cross-browser Support**: Chrome (headless and non-headless modes)
- **Automatic Driver Management**: WebDriverManager handles driver downloads

### 3. Comprehensive Test Coverage
- **End-to-End Workflow**: Complete user journey testing
- **Search Functionality**: Program search and filtering
- **My List Feature**: Adding/removing programs from personal list
- **Account Management**: User registration and authentication

### 4. Advanced Wait Strategies
- **Explicit Waits**: WebDriverWait with custom conditions
- **Element Visibility**: Robust element interaction patterns
- **Timeout Handling**: Configurable timeout values

### 5. Test Data Management
- **Dynamic Data Generation**: Unique test data for each run
- **Realistic Test Data**: Valid email formats and user information

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser installed

### Installation
```bash
# Clone or extract the project
cd epbc-automation-assignment

# Install dependencies
mvn clean compile
```

### Running Tests
```bash
# Run all tests (headless mode)
mvn test -Dheadless=true

# Run tests with browser visible
mvn test -Dheadless=false

# Run specific test class
mvn test -Dtest=EpbcSearchMyListTest
```

## Test Scenarios

### Main Test: `testSearchAndMyListWorkflow`
1. **Navigation**: Navigate to EPBC homepage
2. **Authentication**: Create new user account
3. **Search**: Search for programs using keyword "business"
4. **Filtering**: Apply location filter (Vancouver)
5. **My List**: Add programs to personal list
6. **Verification**: Verify programs appear in My List
7. **Management**: Remove programs from My List

### Additional Test: `testSearchWithoutSignIn`
- Search functionality without user authentication
- Verify search results and filtering capabilities

## Configuration

### Browser Configuration
- **Default**: Chrome browser
- **Headless Mode**: Configurable via system property
- **Window Size**: 1920x1080 for consistent rendering
- **Options**: No sandbox, disable dev-shm-usage for CI/CD compatibility

### Timeout Configuration
- **Page Load**: 30 seconds
- **Element Wait**: 15 seconds
- **Implicit Wait**: 10 seconds

## Reporting

The project generates multiple types of reports:

1. **Surefire Reports**: Located in `target/surefire-reports/`
   - HTML reports with detailed test results
   - XML reports for CI/CD integration
   - Console output logs

2. **TestNG Reports**: Native TestNG HTML reports
   - Test execution timeline
   - Pass/fail statistics
   - Error details and stack traces

3. **Console Logs**: Detailed execution logs with timestamps

## Best Practices Implemented

1. **Page Object Model**: Clean separation of test logic and page interactions
2. **DRY Principle**: Reusable components and utilities
3. **Explicit Waits**: Robust element interaction patterns
4. **Logging**: Comprehensive logging for debugging
5. **Error Handling**: Graceful handling of common web automation issues
6. **Maintainability**: Modular design for easy updates and extensions

## Contributing

When extending this project:

1. Follow the existing Page Object Model pattern
2. Add appropriate logging statements
3. Use explicit waits for element interactions
4. Update documentation for new features
5. Ensure tests are independent and can run in any order

## Support

For issues or questions:
1. Check the TROUBLESHOOTING.md file
2. Review the test execution logs
3. Verify browser and driver compatibility
4. Ensure all dependencies are properly installed

---

*This project demonstrates professional-grade test automation practices suitable for enterprise-level applications.*
