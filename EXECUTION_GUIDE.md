# EPBC Automation - Execution Guide

## Prerequisites

### System Requirements
- **Operating System**: Windows 10+, macOS 10.14+, or Linux (Ubuntu 18.04+)
- **Java**: JDK 11 or higher
- **Maven**: 3.6.0 or higher
- **Chrome Browser**: Latest stable version
- **Memory**: Minimum 4GB RAM recommended
- **Network**: Internet connection for WebDriver downloads and website access

### Verification Commands
```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Chrome version
google-chrome --version  # Linux
# or check via browser: chrome://version/
```

## Installation Steps

### 1. Project Setup
```bash
# Navigate to project directory
cd epbc-automation-assignment

# Verify project structure
ls -la

# Expected output should show:
# - pom.xml
# - src/
# - README.md
# - Other documentation files
```

### 2. Dependency Installation
```bash
# Clean and compile project
mvn clean compile

# Download test dependencies
mvn test-compile

# Verify compilation success
echo $?  # Should return 0 for success
```

## Execution Methods

### Method 1: Standard Test Execution

#### Run All Tests (Recommended)
```bash
# Headless mode (faster, suitable for CI/CD)
mvn test -Dheadless=true

# With browser visible (for debugging)
mvn test -Dheadless=false

# With custom browser
mvn test -Dbrowser=chrome -Dheadless=true
```

#### Run Specific Test Class
```bash
# Run only the main test class
mvn test -Dtest=EpbcSearchMyListTest

# Run specific test method
mvn test -Dtest=EpbcSearchMyListTest#testSearchAndMyListWorkflow
```

### Method 2: TestNG XML Execution
```bash
# Run using TestNG suite configuration
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Method 3: IDE Execution

#### IntelliJ IDEA
1. Right-click on `EpbcSearchMyListTest.java`
2. Select "Run 'EpbcSearchMyListTest'"
3. Or use Ctrl+Shift+F10 (Windows/Linux) or Cmd+Shift+R (Mac)

#### Eclipse
1. Right-click on test class
2. Select "Run As" > "TestNG Test"

### Method 4: Command Line with Custom Parameters
```bash
# Run with specific timeout values
mvn test -Dheadless=true -Dtimeout=20

# Run with debug logging
mvn test -Dheadless=true -X

# Run with specific Maven profile
mvn test -Pheadless-testing
```

## Execution Parameters

### Available System Properties
| Property | Default | Description |
|----------|---------|-------------|
| `browser` | chrome | Browser type (chrome, firefox) |
| `headless` | false | Run browser in headless mode |
| `timeout` | 15 | Element wait timeout in seconds |
| `baseUrl` | https://stg-www.educationplannerbc.ca | Target website URL |

### Usage Examples
```bash
# Custom timeout
mvn test -Dtimeout=30 -Dheadless=true

# Different environment
mvn test -DbaseUrl=https://prod-www.educationplannerbc.ca

# Multiple parameters
mvn test -Dbrowser=chrome -Dheadless=true -Dtimeout=20
```

## Understanding Test Output

### Console Output Interpretation

#### Successful Execution
```
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

#### Failed Execution
```
[ERROR] Tests run: 2, Failures: 1, Errors: 0, Skipped: 1
[ERROR] BUILD FAILURE
```

### Log Levels
- **INFO**: General execution information
- **WARN**: Non-critical issues (e.g., CDP version warnings)
- **ERROR**: Test failures and critical issues

### Common Log Messages
```
[main] INFO ca.epbc.qa.tests.EpbcSearchMyListTest - Setting up test with browser: chrome (headless: true)
[main] INFO ca.epbc.qa.utils.DriverFactory - WebDriver created successfully: chrome
[main] INFO ca.epbc.qa.pages.HomePage - Navigated to homepage: https://stg-www.educationplannerbc.ca
```

## Report Generation and Location

### Surefire Reports
```bash
# Location
target/surefire-reports/

# Key files
├── TEST-TestSuite.xml          # XML format for CI/CD
├── TestSuite.txt              # Plain text summary
├── index.html                 # Main HTML report
├── emailable-report.html      # Email-friendly report
└── testng-results.xml         # TestNG XML results
```

### Accessing Reports
```bash
# Open HTML report in browser
open target/surefire-reports/index.html        # macOS
xdg-open target/surefire-reports/index.html    # Linux
start target/surefire-reports/index.html       # Windows
```

### Custom Report Directory
```bash
# Generate reports in custom location
mvn test -Dreports.directory=/path/to/custom/reports
```

## Parallel Execution

### TestNG Parallel Configuration
```bash
# Run tests in parallel (modify testng.xml)
mvn test -Dparallel=methods -DthreadCount=2
```

### Maven Surefire Parallel
```bash
# Parallel execution at class level
mvn test -Dparallel=classes -DthreadCount=2
```

## Debugging and Troubleshooting

### Debug Mode Execution
```bash
# Enable debug logging
mvn test -X -Dheadless=false

# Run with verbose TestNG output
mvn test -Dtestng.verbose=2
```

### Common Execution Issues

#### Issue 1: WebDriver Not Found
```bash
# Solution: Clear WebDriver cache
rm -rf ~/.cache/selenium/
mvn test -Dheadless=true
```

#### Issue 2: Port Already in Use
```bash
# Solution: Kill existing Chrome processes
pkill -f chrome
mvn test -Dheadless=true
```

#### Issue 3: Memory Issues
```bash
# Solution: Increase Maven memory
export MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=512m"
mvn test -Dheadless=true
```

## CI/CD Integration

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn test -Dheadless=true'
            }
            post {
                always {
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/surefire-reports',
                        reportFiles: 'index.html',
                        reportName: 'Test Report'
                    ])
                }
            }
        }
    }
}
```

### GitHub Actions
```yaml
name: Test Execution
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 11
      uses: actions/setup-java@v2
      with:
        java-version: '11'
        distribution: 'adopt'
    - name: Run tests
      run: mvn test -Dheadless=true
    - name: Upload test results
      uses: actions/upload-artifact@v2
      with:
        name: test-results
        path: target/surefire-reports/
```

## Performance Optimization

### Faster Execution Tips
```bash
# Skip compilation if already done
mvn surefire:test -Dheadless=true

# Use parallel execution
mvn test -Dheadless=true -Dparallel=methods -DthreadCount=2

# Skip dependency resolution
mvn test -o -Dheadless=true
```

### Resource Management
```bash
# Limit memory usage
mvn test -Dheadless=true -Xmx1024m

# Clean up after execution
mvn clean test -Dheadless=true
```

---

*For additional support, refer to TROUBLESHOOTING.md or check the project logs for specific error details.*