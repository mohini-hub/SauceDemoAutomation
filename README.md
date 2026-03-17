# 🛒 SauceDemo Automation Framework

![Java](https://img.shields.io/badge/Java-11-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.18-green)
![TestNG](https://img.shields.io/badge/TestNG-7.9-blue)
![Maven](https://img.shields.io/badge/Maven-3.x-red)
![CI](https://img.shields.io/badge/CI-GitHub%20Actions-black)
![Site](https://img.shields.io/badge/Site-SauceDemo-yellow)

End-to-end automation framework for **https://www.saucedemo.com** using Selenium WebDriver + TestNG + Page Object Model pattern.

---

## 🔧 Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 11 | Programming Language |
| Selenium WebDriver | 4.18.1 | Browser Automation |
| TestNG | 7.9 | Test Framework |
| Maven | 3.x | Build + Dependency Management |
| ExtentReports | 5.1.1 | HTML Test Reports |
| WebDriverManager | 5.7.0 | Auto ChromeDriver Setup |
| Apache POI | 5.2.5 | Excel Data-Driven Testing |
| GitHub Actions | - | CI/CD Pipeline |

---

## 📁 Project Structure

```
SauceDemoAutomation/
├── src/
│   ├── main/java/
│   │   ├── pages/
│   │   │   ├── LoginPage.java       # Login page - POM
│   │   │   ├── ProductsPage.java    # Products + Sort + Cart - POM
│   │   │   └── CartPage.java        # Cart page - POM
│   │   └── utils/
│   │       ├── DriverSetup.java          # Chrome WebDriver config
│   │       ├── ExcelReader.java          # Excel test data reader
│   │       ├── ScreenshotUtil.java       # Auto screenshot on failure
│   │       └── ExtentReportManager.java  # HTML report generator
│   └── test/
│       ├── java/tests/
│       │   ├── BaseTest.java        # Common setup + teardown
│       │   ├── LoginTest.java       # 9 login test cases
│       │   └── ProductTest.java     # 8 product + cart test cases
│       └── resources/testdata/
│           └── LoginData.xlsx       # Data driven test data (5 users)
├── reports/                         # Generated HTML reports + screenshots
├── .github/workflows/test.yml       # CI/CD - auto run on push
├── testng.xml                       # Test suite configuration
├── pom.xml                          # Maven dependencies
└── README.md
```

---

## ✅ Test Coverage — 17 Test Cases

### 🔐 Login Tests (9 cases)
| # | Test | Status |
|---|------|--------|
| 1 | Valid user (standard_user) login | ✅ |
| 2 | Locked out user rejected | ✅ |
| 3 | Wrong password — error message | ✅ |
| 4 | Empty username — validation | ✅ |
| 5 | Empty password — validation | ✅ |
| 6 | Both fields empty | ✅ |
| 7 | Login + Logout flow | ✅ |
| 8 | Problem user login | ✅ |
| 9 | Data Driven — 5 users from Excel | ✅ |

### 🛍️ Product + Cart Tests (8 cases)
| # | Test | Status |
|---|------|--------|
| 1 | Products page — 6 products load | ✅ |
| 2 | Add first product to cart | ✅ |
| 3 | Add multiple products to cart | ✅ |
| 4 | Product visible in cart page | ✅ |
| 5 | Remove item from cart | ✅ |
| 6 | Sort by Name A-Z | ✅ |
| 7 | Sort by Price Low to High | ✅ |
| 8 | Continue Shopping navigation | ✅ |

---

## 🚀 How to Run

### Prerequisites
- Java 11+
- Maven 3.x
- Chrome (latest) — ChromeDriver auto downloads via WebDriverManager

### Run Commands

```bash
# Clone
git clone https://github.com/yourusername/SauceDemoAutomation.git
cd SauceDemoAutomation

# All tests run
mvn test

# Specific test class
mvn test -Dtest=LoginTest
mvn test -Dtest=ProductTest

# Single test method
mvn test -Dtest=LoginTest#testValidLogin

# Report baghaaycha (browser madhe open kara)
open reports/ExtentReport.html
```

---

## 👤 Test Credentials (SauceDemo)

| Username | Password | Type |
|----------|----------|------|
| standard_user | secret_sauce | Valid user ✅ |
| locked_out_user | secret_sauce | Locked ❌ |
| problem_user | secret_sauce | UI issues ⚠️ |

---

## 📊 Sample Report Output

```
=====================================
Total Tests  : 17
Passed       : 15  ✅
Failed       : 2   ❌  
Skipped      : 0   ⏭
Pass Rate    : 88.2%
Duration     : 3m 12s
=====================================
Report: reports/ExtentReport.html
Screenshots: reports/screenshots/
=====================================
```

---

## 🏗️ Design Patterns

- **Page Object Model (POM)** — Locators + actions separate pages madhe
- **Data Driven Testing** — Excel madhu 5 different users test
- **Base Test Class** — Common @BeforeMethod / @AfterMethod inheritance
- **Singleton Pattern** — Single WebDriver + ExtentReport instance

---

## 👩‍💻 Author

**Mohini** — QA Engineer  
2+ years experience | Manual + Automation Testing  
Domains: EdTech | ERP | E-Commerce | Healthcare  
🔗 [LinkedIn Profile](https://linkedin.com/in/yourprofile) | [GitHub](https://github.com/yourusername)
