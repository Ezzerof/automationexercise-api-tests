# AutomationExercise API Test Suite

This repository contains a comprehensive set of **JUnit 5** and **Rest Assured** tests for the [AutomationExercise](https://automationexercise.com) API. These tests focus on various endpoints (brands, products, users, login, update, etc.) and demonstrate data-driven testing using **CSV files**.

---

## Table of Contents

1. [Overview](#overview)
2. [Project Structure](#project-structure)
3. [Key Features](#key-features)
4. [Installation & Setup](#installation--setup)
5. [Running Tests](#running-tests)
6. [CSV Files & Data-Driven Testing](#csv-files--data-driven-testing)
7. [Known Bugs & Discrepancies](#known-bugs--discrepancies)
8. [License](#license)

---

## Overview

The goal of this project is to provide **robust test coverage** for the AutomationExercise API, including:

- **Brands List**  
  - `GetBrandsListTest`  
  - `PutToBrandsListTest`
- **Products List**  
  - `GetProductsListTest`  
  - `PostProductListTest`  
  - `PostToSearchProductTest`  
  - `SearchProductWithoutParameterTest`
- **User Account**  
  - `CreateUserAccountTest`  
  - `DeleteUserAccountTest`  
  - `GetUserAccountTest`  
  - `UpdateUserAccountTest`
- **Login Scenarios**  
  - `ValidLoginTest`  
  - `InvalidLoginTest`

These tests verify both **happy paths** (valid inputs) and **negative scenarios** (invalid/missing parameters).

---

## Project Structure

```bash
.
├── src
│   └── test
│       ├── java
│       │   └── com
│       │       └── automationexercise
│       │           └── api
│       │               ├── config
│       │               │   └── Config.java
│       │               └── csv_path
│       │                   ├── brands_list_tests
│       │                   │   ├── GetBrandsListTest.java
│       │                   │   └── PutToBrandsListTest.java
│       │                   ├── products_list_tests
│       │                   │   ├── GetProductsListTest.java
│       │                   │   ├── PostProductListTest.java
│       │                   │   ├── PostToSearchProductTest.java
│       │                   │   └── SearchProductWithoutParameterTest.java
│       │                   └── user_tests
│       │                       ├── CreateUserAccountTest.java
│       │                       ├── DeleteUserAccountTest.java
│       │                       ├── GetUserAccountTest.java
│       │                       ├── InvalidLoginTest.java
│       │                       ├── UpdateUserAccountTest.java
│       │                       └── ValidLoginTest.java
│       └── resources
│           ├── brands_data.csv
│           ├── create_user_data.csv
│           ├── delete_user_data.csv
│           ├── invalid_login_data.csv
│           ├── update_user_data.csv
│           └── ...
└── README.md
```

## Key Features
- **Data-Driven Testing:**
Each test class uses CSV files to handle multiple scenarios (valid, invalid, missing parameters, etc.).

- **Negative Testing:**
Verifies that the API correctly handles error conditions like invalid credentials or missing parameters.

- **Comprehensive Coverage:**
Covers endpoints for brands, products, search, user account creation/updating/deletion, and login scenarios.

- **Clean Setup & Cleanup:**
Tests create and delete users as needed, ensuring a clean environment for each run.

- **Detailed Documentation:**
Each test class has Javadoc or inline documentation explaining its purpose, usage, and expected outcomes.#

## Installation & Setup
1. Clone this repository
   ``` bash
   git clone https://github.com/ezzerof/automationexercise-api-tests.git
   cd automationexercise-api-tests
   ```
2. Configure the Project:
- Ensure you have **Java 8+** installed.
- Open in **IntelliJ IDEA** or your preferred IDE.
- The project uses **JUnit 5** and **Rest Assured**. Check ```pom.xml``` (Maven) or ```build.gradle``` (Gradle) for dependencies.
3. Update ```Config.java```:
- Adjust **endpoints, CSV paths**, and **expected messages** to match your environment if needed.

## Running Tests
1. Via IDE:
- Right-click on the ```test``` directory (or a specific package/class) → **Run Tests**.
2. Via Command Line:
- Maven:
```bash
mvn clean test
```
- Gradle:
```bash
gradle clean test
```
3. Test Reports:
- For Maven: target/surefire-reports

## CSV Files & Data-Driven Testing
| CSV File | Description | Example Header |
| ---------|-------------|----------------|
|```brands_data.csv```|Expected brand IDs/names for brand tests|```Brand ID,Name```|
|```create_user_data.csv```|Data for creating user accounts|```Name,Email,Password,Title,Birth day,...,City,Mobile number```|
|```delete_user_data.csv```|Emails/passwords for user deletion tests|```Email,Password```|
|```invalid_login_data.csv```|Invalid credentials for negative login scenarios|```Email,Password```|
|```valid_login_data.csv```|Valid credentials for positive login tests|```Email,Password```|
|```update_user_data.csv```|Fields for updating existing user accounts (PUT tests)|```Name,Email,Password,Title,Birth day,...,City,Mobile number```|
|```products_search_data.csv```|Terms for product search tests|```Product name```|

**Note:** Each CSV has a header row. The test classes read these files via ```@CsvFileSource```.

## Known Bugs & Discrepancies
- Missing ```mobile_number``` Field:
In some user detail responses, the ```mobile_number``` field is omitted despite being mentioned in the API documentation. Tests for ```mobile_number``` currently fail due to this discrepancy.

- Price Formatting:
Some product prices include currency symbols (e.g.,```Rs. 500```). The tests clean out non-numeric characters before parsing, but this is considered a partial bug or undocumented behavior in the API.

## License
This project is released under the MIT License. You are free to use, modify, and distribute it under the terms of the license.
