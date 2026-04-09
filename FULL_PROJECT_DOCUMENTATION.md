# Full Project Documentation

## 1. What This Project Is

This project is a Sales Management System built using Spring Boot.

It is made to show how an OLTP system works in real life.

OLTP means Online Transaction Processing.
In simple words, it means:
- Many small operations
- Done very fast
- Done correctly every time

Example operations in this system:
- Add a customer
- Add a product
- Record a sale
- Update stock
- Search data quickly

This project also includes a small Data Warehouse pipeline for analytics.
So this project supports both:
- Day-to-day transactions (OLTP)
- Reporting and analysis (Warehouse)

---

## 2. Main Goal of the Project

The goal is to build a complete mini business system that can:
- Manage customers
- Manage products and stock
- Manage store locations
- Record sales transactions
- Show data in a web UI
- Provide REST APIs for external use
- Compare OLTP query speed with warehouse query speed

This project uses Sri Lankan context and LKR currency.

---

## 3. Technologies Used

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- Spring Web MVC
- Thymeleaf
- Hibernate
- H2 Database
- Lombok
- Maven

Why these are used:
- Spring Boot: fast backend development
- JPA + Hibernate: easy database mapping
- Thymeleaf: server-side web pages
- H2: simple in-memory database for development
- Maven: build and dependency management

---

## 4. Project Structure (Simple View)

- src/main/java/com/oltp
  - SalesSystemApplication.java: application start point
  - config: setup and sample data loading
  - controller: handles HTTP requests
  - dto: response/request helper models
  - entity: database table models
  - repository: database access layer
  - service: business logic

- src/main/resources
  - application.properties: app and database settings
  - templates: HTML pages
  - static/css: CSS styles

- README.md: quick guide
- OLTP_DESIGN.md: design details
- ER_DIAGRAM.md: data relationships
- PHASES.md: project phase breakdown

---

## 5. How the System Works (End-to-End)

A simple flow:

1. User opens web page or calls API.
2. Request goes to a Controller.
3. Controller calls Service.
4. Service applies business rules.
5. Service calls Repository.
6. Repository reads/writes Entity data in database.
7. Result is returned as HTML page or JSON response.

Example: Create Sale
- User submits sale form
- Sales controller receives request
- Sales service validates data
- Service checks product stock
- Service saves sale record
- Service updates stock quantity
- Response returned to user

---

## 6. Core Business Modules

### 6.1 Customer Module

Purpose:
- Store and manage customer details

Common fields:
- Name
- Email
- Phone
- Address
- Status

Actions:
- Add customer
- Edit customer
- Search customer
- Delete customer

### 6.2 Product Module

Purpose:
- Store product details and inventory

Common fields:
- SKU
- Product name
- Category
- Price (LKR)
- Cost price (LKR)
- Stock quantity
- Reorder level

Actions:
- Add product
- Edit product
- Update stock
- Find low stock products
- Delete product

### 6.3 Location Module

Purpose:
- Manage different sales/storage places

Location types:
- Retail
- Warehouse
- Outlet
- Online

Actions:
- Add location
- Edit location
- View active locations
- Delete location

### 6.4 Sales Module

Purpose:
- Record sales transactions

Important fields:
- Order number
- Customer
- Product
- Location
- Quantity
- Unit price
- Discount
- Tax
- Total amount
- Payment method
- Payment status
- Order status

Actions:
- Create sale
- Edit sale
- Find by date range
- Run product-location analytics
- Delete sale

---

## 7. Database Design (Easy Explanation)

The system uses normalized OLTP tables for daily operations.
This helps avoid duplicate data and keeps updates safe.

Main tables:
- customer
- product
- location
- sales

Relationships:
- One customer can have many sales
- One product can appear in many sales
- One location can have many sales

Why this is good:
- Better consistency
- Better transactional behavior
- Easier maintenance

---

## 8. Data Warehouse Part (Phase 3)

This project also has a warehouse flow for analytics.

Pipeline stages:
1. Extract OLTP sales into staging table
2. Load and update dimensions
3. Load fact table
4. Build daily datamart

Warehouse tables:
- stage_sales
- dim_product
- dim_location
- dim_date
- fact_sales
- sales_datamart_daily
- warehouse_pipeline_state

Why warehouse is added:
- Faster reporting queries
- Better aggregation performance
- Easier analytics

---

## 9. REST API Documentation

Base URL:
- http://localhost:8080/api

### 9.1 Customer APIs
- GET /customers
- GET /customers/{id}
- GET /customers/email/{email}
- POST /customers
- PUT /customers/{id}
- DELETE /customers/{id}

### 9.2 Product APIs
- GET /products
- GET /products/{id}
- GET /products/sku/{sku}
- GET /products/category/{category}
- GET /products/reorder
- POST /products
- PUT /products/{id}
- PUT /products/{id}/stock?quantity={n}
- DELETE /products/{id}

### 9.3 Location APIs
- GET /locations
- GET /locations/{id}
- GET /locations/active
- POST /locations
- PUT /locations/{id}
- DELETE /locations/{id}

### 9.4 Sales APIs
- GET /sales
- GET /sales/{id}
- GET /sales/date-range?startDate=...&endDate=...
- GET /sales/product-location?productId={id}&locationId={id}&startDate=...&endDate=...
- GET /sales/analytics/product-location?productId={id}&locationId={id}&startDate=...&endDate=...
- POST /sales
- PUT /sales/{id}
- DELETE /sales/{id}

### 9.5 Benchmark and Warehouse APIs
- GET or POST /benchmark/warehouse/rebuild
- GET or POST /benchmark/warehouse/incremental/run
- GET /benchmark/warehouse/status
- GET /benchmark/warehouse/reconcile?startDate=...&endDate=...
- GET /benchmark/datamart/daily?startDate=...&endDate=...
- GET /benchmark/datamart/top-products?limit=10
- GET /benchmark/datamart/top-locations?limit=10
- GET /benchmark/sales-compare?productId={id}&locationId={id}&startDate=...&endDate=...&runs=30

---

## 10. Web Pages

Main pages in the app:
- Dashboard
- Products
- Customers
- Locations
- Sales

What users can do from UI:
- Add, edit, delete records
- Search and filter data
- See stock alerts
- View transaction information

---

## 11. Setup and Run Guide

### 11.1 Prerequisites
- Java 21+
- Maven 3.9+
- Port 8080 free

### 11.2 Build and Run

Option A (Windows):
- Run: .\run-app.bat

Option B (All platforms):
1. mvn clean install
2. mvn spring-boot:run

### 11.3 Access Application
- App: http://localhost:8080
- H2 console: http://localhost:8080/h2-console

---

## 12. Configuration

Main file:
- src/main/resources/application.properties

Default behavior:
- Uses H2 in-memory DB
- Auto creates schema at startup
- Drops schema at shutdown
- H2 console enabled

If needed, database can be switched to:
- MySQL
- PostgreSQL

You only need to:
- Change datasource properties
- Add correct JDBC dependency in pom.xml

---

## 13. Sample Data

At startup, the app loads around 100 records.

Includes:
- Sri Lankan names
- Sri Lankan cities
- LKR prices
- Multiple products and categories
- Multiple location types
- Example sales transactions

This helps testing immediately without manual data entry.

---

## 14. OLTP Principles Demonstrated

### 14.1 Normalization
Data is split into related tables to avoid duplication.

### 14.2 Indexing
Important columns are indexed for faster search.

### 14.3 Foreign Keys
Sales must refer to valid customer, product, and location.

### 14.4 ACID Transactions
Sales and stock updates remain correct even under concurrent use.

### 14.5 Audit Trail
Created and updated timestamps support traceability.

---

## 15. Performance Benchmarking

The project compares two models:
- OLTP query model
- Dimensional warehouse query model

Expected result:
- For aggregate analytics, dimensional model is often faster

Reason:
- Pre-joined and pre-aggregated structures reduce query cost

---

## 16. Error Handling and Validation

The application includes:
- Basic validation in forms and services
- Proper HTTP endpoints for success/failure states
- Safe CRUD handling for missing IDs and bad requests

For production, more validations and custom exception handlers can be added.

---

## 17. Testing Suggestions

Good tests to add or improve:
- Unit tests for service layer
- Integration tests for repository and API endpoints
- Transaction tests for sale + stock update consistency
- Benchmark repeatability tests

---

## 18. Troubleshooting Guide

### Problem: Port 8080 already in use
Solution:
- Change server.port in application.properties

### Problem: Java version error
Solution:
- Check java -version and ensure version 21+

### Problem: Maven command not found
Solution:
- Install Maven and add to system PATH

### Problem: App not starting
Solution:
- Run with logs using mvn spring-boot:run
- Check first error in console output

### Problem: No sample data visible
Solution:
- Confirm DataLoader runs at startup
- Check H2 console and table counts

---

## 19. Security and Production Notes

Current setup is designed for learning and local development.

Before production use, add:
- Authentication and authorization
- Input sanitization and stricter validation
- External database with backup strategy
- Profile-based config (dev/test/prod)
- Logging and monitoring
- Proper error response standards

---

## 20. Future Improvements

Possible next steps:
- Add role-based access (admin, cashier, manager)
- Add JWT security for APIs
- Add pagination for large datasets
- Add advanced reports and chart dashboards
- Add CI pipeline and test coverage reporting
- Deploy using Docker and cloud database

---

## 21. Quick Glossary

- OLTP: fast daily transaction processing
- CRUD: create, read, update, delete
- DTO: data object used between layers
- JPA: Java API for database mapping
- ETL: extract, transform, load
- Fact table: numeric transaction metrics for analytics
- Dimension table: descriptive analytics context (product, location, date)
- Datamart: prepared summary table for quick reporting

---

## 22. Final Summary

This project is a complete educational example of a modern transaction system.

It teaches:
- How to build an OLTP app with Spring Boot
- How to model core business entities
- How to create web pages and REST APIs
- How to add a simple warehouse pipeline for analytics
- How to compare transactional and analytical query performance

It is practical, structured, and ready for learning, demos, and further extension.
