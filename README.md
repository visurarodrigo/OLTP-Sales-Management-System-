# OLTP Sales Management System

## Overview

The **OLTP Sales Management System** is a complete Spring Boot application that demonstrates how to build a **high-performance inventory and sales management system**. It's designed for real-time operations like processing sales, managing inventory, tracking customers, and managing store locations.

**OLTP** stands for "Online Transaction Processing" - it means the system is optimized to handle many small, fast operations quickly and reliably, just like a real supermarket or retail store.

### What Can You Do With This System?

- ✅ Manage customers (add, edit, search, delete)
- ✅ Manage products and inventory in real-time
- ✅ Track sales transactions with full details
- ✅ View store locations and their details
- ✅ Generate automatic product SKU codes
- ✅ Access data through a beautiful web interface
- ✅ Use REST API for programmatic access
- ✅ Compare performance between OLTP and data warehouse models

---

## Table of Contents

1. [Features](#features)
2. [Prerequisites](#prerequisites)
3. [Quick Start](#quick-start)
4. [Repository Information](#repository-information)
5. [Installation & Setup](#installation--setup)
6. [Project Structure](#project-structure)
7. [Database Schema](#database-schema)
8. [Technology Stack](#technology-stack)
9. [Web User Interface](#web-user-interface)
10. [REST API Endpoints](#rest-api-endpoints)
11. [Sample Data](#sample-data)
12. [OLTP Design Principles](#oltp-design-principles)
13. [Dimensional Model & Warehouse Pipeline (Phase 2 & 3)](#phase-2--phase-3-dimensional-model-and-warehouse-pipeline)
14. [Configuration & Troubleshooting](#configuration--troubleshooting)

---

## Features

### 🎨 Web User Interface
A modern, easy-to-use web dashboard with:

- **Dashboard** - See total products, customers, stores, and sales at a glance
- **Product Management** - Add, edit, delete, and search products easily
  - Auto-generates unique SKU codes
  - Real-time stock tracking
  - Low stock warnings
  
- **Customer Management** - Manage all customer information
  - Search by name or email
  - Add new customers with full details
  - Track customer status (Active, Inactive, Suspended)
  
- **Location Management** - View all store locations
  - Display as easy-to-scan cards
  - See store details and contact info
  
- **Sales Tracking** - View all sales transactions
  - See product, customer, location details
  - Calculate total revenue
  
- **Responsive Design** - Works perfectly on desktop and mobile devices
- **Modern UI** - Purple gradient backgrounds with smooth animations

### 🔌 REST API
A complete API for developers to integrate with the system:

- Create, read, update, delete (CRUD) for all data types
- Search and filter capabilities
- Date range queries
- Product-location sales analytics
- Performance comparison between OLTP and data warehouse models

### 🏗️ Phase 3 Data Warehouse Pipeline

Phase 3 introduces a complete warehouse architecture on top of OLTP:

- Staging extraction table (`stage_sales`)
- Star schema (`dim_product`, `dim_location`, `dim_date`, `fact_sales`)
- Sales datamart (`sales_datamart_daily`)
- Reconciliation and status monitoring endpoints
- Watermark-based incremental refresh (`warehouse_pipeline_state`)

---

## Prerequisites

Before you start, make sure you have:

1. **Java 21 (LTS)** - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
   - Verify with: `java -version`
   
2. **Maven 3.9+** - Build tool for Java projects
   - Verify with: `mvn --version`
   - [Download here](https://maven.apache.org/download.cgi)
   
3. **Port 8080** - Must be available (the app runs on this port)
   - Check on Windows: `netstat -an | findstr 8080`
   - Check on Mac/Linux: `lsof -i :8080`

4. **Git** (optional, but recommended)
   - For cloning the repository
   - [Download here](https://git-scm.com/)

---

## Quick Start

### Option 1: Windows Users (Easiest)
Simply run this batch file:
```cmd
.\run-app.bat
```

### Option 2: All Platforms
```bash
mvn clean install
mvn spring-boot:run
```

### What Happens After You Start?

1. Application starts on `http://localhost:8080`
2. Database initializes automatically (H2 in-memory)
3. 100 sample customers, products, locations, and sales are loaded
4. You can now access the web interface and REST API

### Stop the Application
Press `Ctrl+C` in the terminal

### Access Points Once Running

| Feature | URL |
|---------|-----|
| Dashboard | http://localhost:8080 |
| Products | http://localhost:8080/products |
| Customers | http://localhost:8080/customers |
| Locations | http://localhost:8080/locations |
| Sales | http://localhost:8080/sales |
| H2 Database Console | http://localhost:8080/h2-console |
| REST API | http://localhost:8080/api |

---

## Repository Information

### About This Project

- **GitHub Repository:** [OLTP-Sales-Management-System](https://github.com/visurarodrigo/OLTP-Sales-Management-System-)
- **Main Branch:** `main` (stable, production-ready code)
- **License:** Educational and open-source
- **Author:** Visura Rodrigo
- **Last Updated:** March 23, 2026

### Getting the Code

**Clone using HTTPS:**
```bash
git clone https://github.com/visurarodrigo/OLTP-Sales-Management-System-.git
cd "OLTP-Sales-Management-System-"
```

**Clone using SSH (if configured):**
```bash
git clone git@github.com:visurarodrigo/OLTP-Sales-Management-System-.git
cd "OLTP-Sales-Management-System-"
```

### Git Commands

**View commit history:**
```bash
git log --oneline -10
```

**View current branch:**
```bash
git branch --show-current
```

**Check status:**
```bash
git status
```

**Pull latest changes:**
```bash
git pull origin main
```

---

## Installation & Setup

This section provides detailed step-by-step instructions for setting up the project on your computer.

### Step 1: Install Java 21

**Windows:**
1. Download Java 21 from [Oracle Downloads](https://www.oracle.com/java/technologies/downloads/)
2. Run the installer (.exe file)
3. Follow the installation wizard
4. Restart your computer

**Mac (using Homebrew):**
```bash
brew install java
```

**Ubuntu/Debian:**
```bash
sudo apt-get update
sudo apt-get install openjdk-21-jdk
```

**Verify Installation:**
```bash
java -version
javac -version
```

Should show Java 21.x

### Step 2: Install Maven 3.9+

**Windows (using Chocolatey):**
```powershell
choco install maven -y
```

**Windows (Manual):**
1. Download from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Add environment variable:
   - Right-click "This PC" → Properties → Environment Variables
   - New variable: `MAVEN_HOME` = `C:\Program Files\Apache\maven`
   - Add to `PATH`: `;%MAVEN_HOME%\bin`

**Mac:**
```bash
brew install maven
```

**Ubuntu/Debian:**
```bash
sudo apt-get install maven
```

**Verify Installation:**
```bash
mvn --version
```

Should show Maven 3.9.x or higher

### Step 3: Clone the Project

```bash
git clone https://github.com/visurarodrigo/OLTP-Sales-Management-System-.git
cd "OLTP-Sales-Management-System-"
```

### Step 4: Build the Project

```bash
mvn clean install
```

This downloads all dependencies and compiles the project. Takes about 2-3 minutes on first run.

### Step 5: Run the Application

**Option A (Windows):**
```cmd
.\run-app.bat
```

**Option B (All platforms):**
```bash
mvn spring-boot:run
```

**Option C (Make a JAR and run):**
```bash
mvn clean package
java -jar target/oltp-0.0.1-SNAPSHOT.jar
```

### Step 6: Verify It's Running

1. Open browser to `http://localhost:8080`
2. You should see the dashboard with statistics
3. Try clicking through Products, Customers, Locations, Sales
4. All should have sample data pre-loaded

---

## Project Structure

Here's how the code is organized:

```
OLTP-Sales-Management-System/
│
├── src/main/java/com/oltp/
│   ├── SalesSystemApplication.java        ← Main app entry point
│   │
│   ├── config/
│   │   └── DataLoader.java                ← Loads 100 sample records on startup
│   │
│   ├── controller/                        ← Handles web requests
│   │   ├── HomeController.java            ← Dashboard page
│   │   ├── WebProductController.java      ← Product web pages
│   │   ├── WebCustomerController.java     ← Customer web pages
│   │   ├── WebLocationController.java     ← Location web pages
│   │   ├── WebSalesController.java        ← Sales web pages
│   │   ├── ProductController.java         ← Product REST API
│   │   ├── CustomerController.java        ← Customer REST API
│   │   ├── LocationController.java        ← Location REST API
│   │   ├── SalesController.java           ← Sales REST API
│   │   └── BenchmarkController.java       ← Performance comparison API
│   │
│   ├── entity/                            ← Database table models
│   │   ├── Customer.java                  ← Customer entity
│   │   ├── Product.java                   ← Product entity
│   │   ├── Location.java                  ← Store location entity
│   │   ├── Sales.java                     ← Sales transaction entity
│   │   ├── StageSales.java                ← Staging sales extraction table
│   │   ├── DimProduct.java                ← Data warehouse product dimension
│   │   ├── DimLocation.java               ← Data warehouse location dimension
│   │   ├── DimDate.java                   ← Data warehouse date dimension
│   │   ├── FactSales.java                 ← Data warehouse sales facts
│   │   ├── SalesDatamartDaily.java        ← Daily sales datamart table
│   │   └── WarehousePipelineState.java    ← Incremental watermark state
│   │
│   ├── repository/                        ← Database queries
│   │   ├── CustomerRepository.java        ← Query customer data
│   │   ├── ProductRepository.java         ← Query product data
│   │   ├── LocationRepository.java        ← Query location data
│   │   ├── SalesRepository.java           ← Query sales data
│   │   ├── StageSalesRepository.java      ← Query staging sales
│   │   ├── DimProductRepository.java      ← Query product dimension
│   │   ├── DimLocationRepository.java     ← Query location dimension
│   │   ├── DimDateRepository.java         ← Query date dimension
│   │   ├── FactSalesRepository.java       ← Query sales facts
│   │   ├── SalesDatamartDailyRepository.java ← Query datamart rows
│   │   └── WarehousePipelineStateRepository.java ← Query incremental watermark
│   │
│   ├── service/                           ← Business logic
│   │   ├── CustomerService.java           ← Customer operations
│   │   ├── ProductService.java            ← Product operations
│   │   ├── LocationService.java           ← Location operations
│   │   ├── SalesService.java              ← Sales operations
│   │   ├── QueryPerformanceService.java   ← OLTP vs dimensional benchmark logic
│   │   └── WarehouseService.java          ← Phase 3 pipeline and datamart logic
│   │
│   └── dto/                               ← Data transfer objects
│       ├── QueryPerformanceComparisonResponse.java
│       ├── ProductLocationSalesAnalytics.java
│       ├── AggregateSalesResult.java
│       ├── QueryModelPerformance.java
│       ├── WarehousePipelineStatusResponse.java
│       └── WarehouseReconciliationResponse.java
│
├── src/main/resources/
│   ├── application.properties              ← App configuration (DB, port, etc)
│   │
│   ├── templates/                          ← Web pages (HTML)
│   │   ├── index.html                     ← Dashboard page
│   │   ├── products.html                  ← Product list page
│   │   ├── product-form.html              ← Add/Edit product form
│   │   ├── product-details.html           ← Product detail view
│   │   ├── customers.html                 ← Customer list page
│   │   ├── customer-form.html             ← Add/Edit customer form
│   │   ├── locations.html                 ← Store locations page
│   │   └── sales.html                     ← Sales transactions page
│   │
│   └── static/
│       └── css/
│           └── modern-ui.css              ← Styling (gradients, animations)
│
├── pom.xml                                 ← Maven dependencies
├── run-app.bat                             ← Windows startup script
├── .gitignore                              ← Git ignore rules
├── README.md                               ← This file
├── OLTP_DESIGN.md                          ← Detailed design documentation
├── ER_DIAGRAM.md                           ← Entity relationship diagram
├── PHASES.md                               ← Project phases documentation
└── Report.pdf                              ← Project report

```

### Folder Descriptions

**`controller/`** - Where user requests come in
- Web controllers handle page requests (e.g., "show me products page")
- REST API controllers handle API requests (e.g., "give me all products as JSON")

**`entity/`** - Represents database tables
- Each entity = one database table
- Has attributes like name, email, price, etc.

**`repository/`** - Database queries
- Fetch data from database
- Search, filter, sort operations

**`service/`** - Business logic
- What actions can we perform? (e.g., "is stock low?" or "calculate total price")
- Called by controllers and other services

**`resources/`** - Web pages and configuration
- HTML templates for web pages
- CSS for styling
- application.properties for settings

---

## Database Schema

The database has been designed following OLTP principles (optimized for fast transactions). Here's what each table contains:

### 1. Customer Table

Stores information about customers.

**Fields:**
```
customer_id          → Unique ID (auto-generated)
first_name          → Customer's first name
last_name           → Customer's last name
email               → Email address (indexed for fast search)
phone               → Phone number
date_of_birth       → Date of birth for demographics
address             → Street address
city                → City (Sri Lankan cities preferred)
state               → State/Province
country             → Country (usually "Sri Lanka")
postal_code         → Postal code
customer_status     → ACTIVE, INACTIVE, or SUSPENDED
created_at          → When customer was created (auto)
updated_at          → When customer was last updated (auto)
```

**Why Optimized?**
- Email is indexed → Finding customers by email is fast
- Status field → Quick filtering for active/inactive customers
- Normalized → No duplicate data

**Example Data:**
```
ID: 1
Name: Ravi Jayaraman
Email: ravi.jayaraman@example.lk
Phone: +94-11-2345678
City: Colombo
Status: ACTIVE
```

### 2. Product Table

Stores information about products/items for sale.

**Fields:**
```
product_id          → Unique ID (auto-generated)
sku                 → Stock Keeping Unit (unique code, indexed)
product_name        → Name of product
description         → What is this product?
category            → Category name (e.g., "Electronics")
sub_category        → Sub-category (e.g., "Laptops")
price               → Selling price in LKR
cost_price          → Cost to business in LKR
stock_quantity      → How many in stock RIGHT NOW
reorder_level       → Alert when stock falls below this
product_status      → AVAILABLE, OUT_OF_STOCK, DISCONTINUED
brand               → Brand name
weight              → Weight in kg
created_at          → When product was added (auto)
updated_at          → When product was last updated (auto)
```

**Why Optimized?**
- SKU is indexed → Finding by SKU is fast (used at checkout)
- Real-time stock → Immediate updates when items sell
- Category indexed → Fast filtering by category
- Reorder level → Automatic alerts for low stock

**Example Data:**
```
ID: 1
SKU: PROD-2026-001
Name: Samsung 27" Monitor
Price: 45,000 LKR
Stock: 42 units
Category: Electronics → Monitors
Brand: Samsung
Reorder Level: 10 units
```

### 3. Location Table

Stores information about store locations, warehouses, and channels.

**Fields:**
```
location_id         → Unique ID (auto-generated)
store_code          → Unique store code (indexed)
store_name          → Name of the store
location_type       → RETAIL, WAREHOUSE, OUTLET, or ONLINE
address             → Street address in Sri Lanka
city                → City
state               → State/Province
country             → Country
postal_code         → Postal code
phone               → Store phone number
email               → Store email
manager_name        → Name of store manager
opening_time        → When store opens (e.g., 09:00)
closing_time        → When store closes (e.g., 21:00)
store_capacity      → Capacity or square footage
location_status     → ACTIVE, INACTIVE, UNDER_RENOVATION
created_at          → When location was added (auto)
updated_at          → When location was last updated (auto)
```

**Why Optimized?**
- Store code is indexed → Fast location lookups
- Location type → Quick filtering (find all retail stores, all warehouses, etc.)
- Status field → Active/inactive filtering

**Example Data:**
```
ID: 1
Code: COL-001
Name: Colombo Main Store
Type: RETAIL
City: Colombo
Manager: Keshan Perera
Hours: 09:00 - 21:00
Status: ACTIVE
```

### 4. Sales Table

Records every single sales transaction.

**Fields:**
```
sale_id             → Unique ID (auto-generated)
order_number        → Unique order number (e.g., ORD-2026-001)
customer_id         → Which customer? (links to Customer table)
product_id          → Which product? (links to Product table)
location_id         → Which store? (links to Location table)
quantity            → How many items sold?
unit_price          → Price per item (in LKR)
subtotal            → quantity × unit_price
discount_amount     → Discount given (in LKR)
tax_amount          → Tax added (in LKR)
total_amount        → Final amount paid (in LKR)
payment_method      → CASH, CREDIT_CARD, DEBIT_CARD, DIGITAL_WALLET
payment_status      → PAID, PENDING, REFUNDED, FAILED
order_status        → COMPLETED, PROCESSING, CANCELLED, RETURNED
sale_date           → When sale happened (indexed for fast date queries)
delivery_date       → When product was delivered
notes               → Extra notes about this sale
created_at          → When record was created (auto)
updated_at          → When record was last updated (auto)
```

**Why Optimized?**
- Multiple indexes (date, customer, product, location) → Fast queries
- Foreign keys → Maintains relationship integrity (can't have sales for non-existent products)
- Status fields → Quick workflow management
- Sale date indexed → Fast reports for "sales this month"

**Example Data:**
```
ID: 1
Order: ORD-2026-00001
Customer: Ravi Jayaraman (ID: 1)
Product: Samsung Monitor (ID: 1)
Location: Colombo Main Store (ID: 1)
Quantity: 1
Unit Price: 45,000 LKR
Discount: 2,000 LKR
Tax: 7,200 LKR
Total: 50,200 LKR
Payment: CREDIT_CARD (PAID)
Status: COMPLETED
Date: 2026-03-23 14:30:00
```

### Dimensional Model Tables (Data Warehouse)

For Phase 2 and Phase 3 analysis, the project includes these warehouse layers:

**`stage_sales`** - Staging extraction layer
- Raw sales facts extracted from OLTP
- Supports controlled ETL flow and incremental sync

**`dim_product`** - Product dimension (slowly changing)
**`dim_location`** - Location dimension (slowly changing)
**`dim_date`** - Date dimension (all dates for fast date queries)
**`fact_sales`** - Sales facts (optimized for queries)

**`sales_datamart_daily`** - Daily aggregated sales datamart
- Fast read model for analytics endpoints

**`warehouse_pipeline_state`** - Incremental pipeline watermark
- Stores latest successful source update timestamp

These are auto-populated from OLTP tables and used for analytics.

---

## Technology Stack

Here's what technologies power this application:

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 21 (LTS) | Programming language |
| **Spring Boot** | 3.5.0 | Web application framework |
| **Spring Data JPA** | 3.5.0 | Database access (ORM) |
| **Spring Web MVC** | 3.5.0 | Web framework and REST API |
| **Thymeleaf** | 3.1.x | Server-side HTML template engine |
| **Hibernate** | 6.4.x | ORM framework (JPA implementation) |
| **H2 Database** | 2.2.x | In-memory database (dev/testing) |
| **Lombok** | 1.18.x | Reduces boilerplate (getters, setters, etc.) |
| **Jackson** | 2.15.x | JSON serialization/deserialization |
| **Maven** | 3.9+ | Build and dependency management |

### What Does Each Do?

- **Java 21 (LTS)** - Latest stable Java version, good performance
- **Spring Boot** - Makes building web apps easy, includes embedded server
- **Spring Data JPA** - Simplifies writing database queries (you write less code)
- **Hibernate** - Automatic SQL generation from Java objects
- **H2 Database** - Temporary database for development (switches to MySQL/PostgreSQL in production)
- **Thymeleaf** - Creates HTML pages dynamically on the server
- **Lombok** - Auto-generates common methods to save typing
- **Maven** - Downloads libraries and manages dependencies automatically

---

## Web User Interface

The application has a modern web dashboard for users to interact with data.

### Dashboard (http://localhost:8080)

**What You See:**
- Total number of products
- Total number of customers
- Total number of sales
- Total number of locations
- Cards with links to manage each section
- Modern gradient background (purple theme)

**Features:**
- Responsive design (works on phones, tablets, computers)
- Smooth animations and transitions
- Quick navigation to all sections

### Product Management (http://localhost:8080/products)

**What You Can Do:**
- See list of all products in a table
- Search for products by name or SKU (real-time)
- Add a new product with a form
  - SKU is auto-generated
  - Fill in name, category, price, stock, etc.
- Click on a product to see details
- Edit product information
- Delete products (with confirmation)
- Visual badges for low stock warnings

**Example:**
```
Product Name: Samsung 27" Monitor
SKU: PROD-2026-001 (auto-generated)
Category: Electronics → Monitors
Price: 45,000 LKR
Stock: 42 units ✓ In Stock
Brand: Samsung
```

### Customer Management (http://localhost:8080/customers)

**What You Can Do:**
- See all customers in a table format
- Search by name or email (real-time)
- Add new customer
  - Enter first/last name, email, phone
  - Assign status (Active, Inactive, Suspended)
  - Enter address details
- Edit customer information
- Delete customers (with confirmation)

**Example:**
```
Name: Ravi Jayaraman
Email: ravi.jayaraman@example.lk
Phone: +94-11-2345678
City: Colombo
Status: ACTIVE
```

### Location Management (http://localhost:8080/locations)

**What You See:**
- All store locations displayed as cards
- Each card shows:
  - Store name
  - Location type (Retail, Warehouse, Outlet, Online)
  - City, state, country
  - Postal code
  - Contact phone and email
- Clean, easy-to-scan design

**Example:**
```
┌─────────────────────────────────────┐
│   Colombo Main Store                │
│   Retail Store                      │
│   Colombo, Western Province, SL     │
│   10001                             │
│   ☎ (011) 234-5678                 │
│   ✉ colombo@example.lk             │
└─────────────────────────────────────┘
```

### Sales Transactions (http://localhost:8080/sales)

**What You See:**
- Table of all sales transactions
- For each sale:
  - Order number
  - Customer name
  - Product name
  - Quantity sold
  - Price per unit
  - Total amount
  - Store location
  - Date/time
- Summary cards showing:
  - Total transactions (all-time)
  - Total revenue (in LKR)

**Example:**
```
Order: ORD-2026-00001
Customer: Ravi Jayaraman
Product: Samsung 27" Monitor
Quantity: 1
Price: 45,000 LKR
Location: Colombo Main Store
Date: 2026-03-23 14:30
Status: COMPLETED ✓
```

### UI Features

- ✅ Responsive layout (auto-adjusts to screen size)
- ✅ Modern design (gradients, shadows, animations)
- ✅ Real-time search (filters as you type)
- ✅ Form validation (shows errors before submission)
- ✅ Confirmation dialogs (prevent accidental deletions)
- ✅ Status badges (visual indicators for different states)
- ✅ Easy navigation (menu bar with all sections)

---

## REST API Endpoints

The REST API allows programs to interact with the system. All responses are in JSON format.

### Base URL
```
http://localhost:8080/api
```

### Customer Endpoints

**Get all customers:**
```
GET /api/customers
```
Returns: List of all customers

**Get specific customer:**
```
GET /api/customers/{id}
```
Example: `GET /api/customers/1`
Returns: One customer with ID 1

**Get customer by email:**
```
GET /api/customers/email/{email}
```
Example: `GET /api/customers/email/ravi.jayaraman@example.lk`
Returns: Customer with that email

**Create new customer:**
```
POST /api/customers
Content-Type: application/json

{
  "firstName": "Ravi",
  "lastName": "Jayaraman",
  "email": "ravi@example.lk",
  "phone": "+94-11-2345678",
  "address": "123 Main Street",
  "city": "Colombo",
  "state": "Western",
  "country": "Sri Lanka",
  "postalCode": "10001",
  "customerStatus": "ACTIVE"
}
```
Returns: New customer created with ID

**Update customer:**
```
PUT /api/customers/{id}
Content-Type: application/json

{
  "firstName": "Ravi",
  "lastName": "Jayaraman",
  "customerStatus": "INACTIVE"
}
```
Example: `PUT /api/customers/1`
Returns: Updated customer

**Delete customer:**
```
DELETE /api/customers/{id}
```
Example: `DELETE /api/customers/1`
Returns: Success message

---

### Product Endpoints

**Get all products:**
```
GET /api/products
```
Returns: List of all products

**Get specific product:**
```
GET /api/products/{id}
```
Example: `GET /api/products/1`

**Get product by SKU:**
```
GET /api/products/sku/{sku}
```
Example: `GET /api/products/sku/PROD-2026-001`

**Get products by category:**
```
GET /api/products/category/{category}
```
Example: `GET /api/products/category/Electronics`
Returns: All products in Electronics

**Get products needing restock:**
```
GET /api/products/reorder
```
Returns: Products where stock ≤ reorder_level

**Create new product:**
```
POST /api/products
Content-Type: application/json

{
  "productName": "Samsung 27\" Monitor",
  "description": "Full HD IPS Monitor",
  "category": "Electronics",
  "subCategory": "Monitors",
  "price": 45000,
  "costPrice": 30000,
  "stockQuantity": 42,
  "reorderLevel": 10,
  "brand": "Samsung",
  "weight": 4.5
}
```
Returns: New product with auto-generated SKU

**Update product:**
```
PUT /api/products/{id}
Content-Type: application/json

{
  "productName": "Samsung 27\" Monitor - Updated",
  "price": 44500
}
```

**Update stock quantity:**
```
PUT /api/products/{id}/stock?quantity=50
```
Example: `PUT /api/products/1/stock?quantity=40`
Sets stock to exactly 40 units

**Delete product:**
```
DELETE /api/products/{id}
```

---

### Location Endpoints

**Get all locations:**
```
GET /api/locations
```

**Get specific location:**
```
GET /api/locations/{id}
```

**Get only active locations:**
```
GET /api/locations/active
```

**Create new location:**
```
POST /api/locations
Content-Type: application/json

{
  "storeName": "Colombo Main Store",
  "locationType": "RETAIL",
  "address": "123 Main Street",
  "city": "Colombo",
  "state": "Western",
  "country": "Sri Lanka",
  "postalCode": "10001",
  "phone": "+94-11-234-5678",
  "email": "colombo@example.lk",
  "managerName": "Keshan Perera",
  "openingTime": "09:00",
  "closingTime": "21:00"
}
```

**Update location:**
```
PUT /api/locations/{id}
```

**Delete location:**
```
DELETE /api/locations/{id}
```

---

### Sales Endpoints

**Get all sales:**
```
GET /api/sales
```

**Get specific sale:**
```
GET /api/sales/{id}
```

**Get sales by date range:**
```
GET /api/sales/date-range?startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59
```

**Get sales for specific product in location:**
```
GET /api/sales/product-location?productId=1&locationId=1&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59
```

**Get analytics (totals) for product at location:**
```
GET /api/sales/analytics/product-location?productId=1&locationId=1&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59
```
Returns:
```json
{
  "totalQuantity": 50,
  "totalRevenue": 2250000,
  "transactionCount": 5,
  "averageOrderValue": 450000
}
```

**Create new sale:**
```
POST /api/sales
Content-Type: application/json

{
  "customerId": 1,
  "productId": 1,
  "locationId": 1,
  "quantity": 1,
  "unitPrice": 45000,
  "discountAmount": 2000,
  "taxAmount": 7200,
  "paymentMethod": "CREDIT_CARD",
  "paymentStatus": "PAID",
  "orderStatus": "COMPLETED"
}
```

**Update sale:**
```
PUT /api/sales/{id}
```

**Delete sale:**
```
DELETE /api/sales/{id}
```

---

### Benchmark Endpoints

These endpoints support full Phase 3 warehouse operations.

**Pipeline execution:**
```
GET or POST /api/benchmark/warehouse/rebuild
```
Runs full pipeline (OLTP -> staging -> star schema -> datamart).

```
GET or POST /api/benchmark/warehouse/incremental/run
```
Runs incremental extraction from OLTP changes using watermark and refreshes downstream tables.

**Step-by-step pipeline operations:**
```
POST /api/benchmark/warehouse/staging/load
POST /api/benchmark/warehouse/star/rebuild
POST /api/benchmark/warehouse/datamart/refresh
```

**Pipeline monitoring and quality checks:**
```
GET /api/benchmark/warehouse/status
GET /api/benchmark/warehouse/reconcile?startDate=2026-03-01&endDate=2026-03-31
```

**Datamart analytics endpoints:**
```
GET /api/benchmark/datamart/daily?startDate=2026-03-01&endDate=2026-03-31
GET /api/benchmark/datamart/daily?startDate=2026-03-01&endDate=2026-03-31&productId=1&locationId=1
GET /api/benchmark/datamart/top-products?startDate=2026-03-01&endDate=2026-03-31&limit=10
GET /api/benchmark/datamart/top-locations?startDate=2026-03-01&endDate=2026-03-31&limit=10
```

**Performance comparison endpoint:**
```
GET /api/benchmark/sales-compare?productId=1&locationId=1&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59&runs=30
```

Parameters:
- `productId` - Product to compare
- `locationId` - Location to compare
- `startDate` - Start of date range
- `endDate` - End of date range
- `runs` - Number of benchmark runs for averaging

---

## Example API Usage

### Example 1: Get all customers
```bash
curl http://localhost:8080/api/customers
```

Response:
```json
[
  {
    "customerId": 1,
    "firstName": "Ravi",
    "lastName": "Jayaraman",
    "email": "ravi@example.lk",
    "phone": "+94-11-2345678",
    "city": "Colombo",
    "customerStatus": "ACTIVE"
  },
  ...
]
```

### Example 2: Create a new customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ravi",
    "lastName": "Jayaraman",
    "email": "ravi.jayaraman@example.lk",
    "phone": "+94-11-2345678",
    "city": "Colombo",
    "customerStatus": "ACTIVE"
  }'
```

### Example 3: Get products needing restock
```bash
curl http://localhost:8080/api/products/reorder
```

### Example 4: Search for a product by SKU
```bash
curl http://localhost:8080/api/products/sku/PROD-2026-001
```

### Example 5: Get sales for March 2026 at Colombo store
```bash
curl "http://localhost:8080/api/sales/product-location?productId=1&locationId=1&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59"
```

### Example 6: Compare performance (OLTP vs Data Warehouse)
```bash
curl "http://localhost:8080/api/benchmark/sales-compare?productId=1&locationId=1&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59&runs=30"
```

---

## Sample Data

When you start the application, it automatically loads 100 records of each type:

### Sample Customers (Sri Lankan Context)
- Names: Ravi Jayaraman, Sasha Perera, Dilkshan Silva, Priya Mendis, etc.
- Cities: Colombo, Kandy, Galle, Jaffna, Matara, etc.
- Emails, phones, addresses all realistic

### Sample Products
- Electronics (monitors, keyboards, mice)
- Furniture (chairs, desks, tables)
- Office Supplies (pens, paper, folders)
- Food & Beverages
- Clothing
- Each with realistic prices in LKR

### Sample Locations (Stores)
- Colombo Main Store (Retail)
- Kandy Branch (Retail)
- Galle Outlet (Outlet)
- Central Warehouse (Warehouse)
- Online Store (Online)

### Sample Sales Transactions
- 100 realistic sales records
- Various customers, products, locations
- Mix of payment methods and statuses
- Dates distributed across March 2026

**Purpose:** You have data to work with and test immediately without manual entry.

---

## OLTP Design Principles

This project demonstrates best practices for building high-performance transaction processing systems.

### 1. Normalization (3NF)

**What it means:** Eliminate duplicate data

**How we do it:**
- Customer info stored once in Customer table
- When we make a sale, we just store customer_id (not repeat all customer details)
- If customer email changes, we update ONE place, not multiple sales records

**Benefits:**
- Data consistency (no conflicting info)
- Less storage needed
- Updates are fast and safe

**Example:**
Instead of:
```
Sale 1: Customer Name="Ravi", Sale Data...
Sale 2: Customer Name="Ravi", Sale Data...
Sale 3: Customer Name="Ravi", Sale Data...
```

We store:
```
Customer: ID=1, Name="Ravi"
Sale 1: customer_id=1, Sale Data...
Sale 2: customer_id=1, Sale Data...
Sale 3: customer_id=1, Sale Data...
```

### 2. Indexing Strategy

**What it means:** Create shortcuts in the database for fast lookups

**Indexes we use:**
- Primary Key (customer_id, product_id, etc.) → Fastest lookup
- Email → Find customers by email (used at checkout)
- SKU → Find products by code (used at checkout)
- sale_date → Find sales by date (used for reports)
- Status fields → Filter by status quickly

**Benefits:**
- 10-100x faster queries
- Real-time responsiveness even with thousands of records
- No noticeable lag at checkout

**Example:**
```
Without index: Check every sale one-by-one (slow for 100,000 sales)
With index:    Jump directly to sales in March 2026 (instant)
```

### 3. Foreign Key Constraints

**What it means:** Maintain relationships between tables

**How we do it:**
- Sales.customer_id must reference an actual Customer
- Sales.product_id must reference an actual Product
- If you try to delete a customer with open sales, it's prevented

**Benefits:**
- Data integrity (no orphaned records)
- Consistent relationships
- Database enforces rules automatically

### 4. ACID Transactions

**What it means:** Guarantee data safety during complex operations

**The four principles:**
- **Atomicity** - Operation either fully succeeds or fully rolls back (no half-completed sales)
- **Consistency** - Data is always in valid state (stock + sales always match)
- **Isolation** - Operations don't interfere with each other (two checkout processes don't mix)
- **Durability** - Once saved, data survives crashes (reliable backup)

**Benefits:**
- Money is never lost
- Inventory is always accurate
- Zero data corruption risk

### 5. Audit Trail

**What it means:** Track when data changes

**How we do it:**
- Every table has `created_at` and `updated_at` fields
- Automatically updated by the database
- Shows history of changes

**Benefits:**
- Trace what happened and when
- Audit compliance (required for businesses)
- Debugging issues

**Example:**
```
Customer record:
  Created: 2026-01-15 10:30:00 (when customer joined)
  Updated: 2026-03-23 14:45:00 (customer updated email)
```

### 6. Real-Time Operations

**What it means:** Instant updates visible to everyone

**How we do it:**
- Stock updates immediately when sale completes
- Inventory dashboard shows current numbers
- No lag or delay

**Benefits:**
- No overselling (selling items not in stock)
- Accurate inventory reports
- Customer knows real inventory before purchase

---

## Configuration & Troubleshooting

### Configuration File

The main configuration is in `src/main/resources/application.properties`:

```properties
# Server
server.port=8080
server.servlet.context-path=

# Database (H2 - In Memory)
spring.datasource.url=jdbc:h2:mem:oltp_sales_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

# H2 Console (web interface to database)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Switching to MySQL

To use MySQL instead of H2:

1. Install MySQL Server
2. Create database: `CREATE DATABASE oltp_sales_db;`
3. Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/oltp_sales_db
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

4. Add to `pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
</dependency>
```

### Switching to PostgreSQL

1. Install PostgreSQL
2. Create database: `CREATE DATABASE oltp_sales_db;`
3. Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/oltp_sales_db
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

4. Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.0</version>
</dependency>
```

### Troubleshooting

**Problem: Port 8080 already in use**
```
Solution: Change port in application.properties
server.port=8081
(Use any free port)
```

**Problem: Java version error**
```
Solution: Make sure Java 21 is installed
java -version (should show 21.x.x)
```

**Problem: Maven not found**
```
Solution: Install Maven or add to PATH
mvn --version (should show 3.9+)
```

**Problem: Application won't start**
```
Check logs for errors:
mvn spring-boot:run 2>&1 | tail -20
```

**Problem: Can't access http://localhost:8080**
```
Solutions:
1. Check if app is running (no errors in console)
2. Check if port 8080 is open (not blocked by firewall)
3. Wait 5-10 seconds for app to fully start
```

**Problem: No sample data showing**
```
The DataLoader automatically loads data.
Check:
1. H2 console: http://localhost:8080/h2-console
2. API: http://localhost:8080/api/customers
```

---

## Phase 2 & Phase 3: Dimensional Model and Warehouse Pipeline

This project now includes both:

- **Phase 2**: Dimensional model for OLTP vs warehouse comparison
- **Phase 3**: Full warehouse pipeline (staging + star schema + datamart + incremental sync)

### What's the Difference?

**OLTP (Operational):**
- Optimized for fast writes (saving sales)
- Normalized (less storage)
- Used for daily operations
- Many small queries

**Dimensional/Warehouse:**
- Optimized for fast reads (analyzing data)
- Denormalized (duplicates for speed)
- Used for reports and analytics
- Few large queries

### Warehouse Layers

**`stage_sales`** - Staging table
- Extracted from OLTP sales
- Used as controlled input for warehouse loads

**`warehouse_pipeline_state`** - Pipeline watermark state
- Keeps latest successful `updated_at` from OLTP sales
- Enables incremental refresh without full rebuild

### The Star Schema Tables

**`dim_product`** - Product dimension
- Product details slowly change
- Used in many queries
- Improves query performance

**`dim_location`** - Location dimension
- Store details
- Used to group sales by location
- Supports regional analysis

**`dim_date`** - Date dimension
- Pre-calculated dates
- Includes year, month, quarter, day of week
- Makes date queries very fast

**`fact_sales`** - Sales facts
- Contains sale amount, quantity, count
- Links to all dimensions
- Optimized for sum/aggregate queries

### Datamart Table

**`sales_datamart_daily`** - Daily aggregate datamart
- Pre-aggregated quantity, revenue, transaction counts
- Used for top products, top locations, daily analytics

### How to Use

Run full pipeline:

```bash
http://localhost:8080/api/benchmark/warehouse/rebuild
```

Run incremental pipeline:

```bash
http://localhost:8080/api/benchmark/warehouse/incremental/run
```

Check status and reconciliation:

```bash
http://localhost:8080/api/benchmark/warehouse/status
http://localhost:8080/api/benchmark/warehouse/reconcile?startDate=2026-03-01&endDate=2026-03-31
```

Compare performance:
```bash
curl "http://localhost:8080/api/benchmark/sales-compare?productId=1&locationId=1&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59&runs=30"
```

You'll see:
- OLTP query time
- Dimensional model query time
- Percentage improvement

### Example Benchmark Response (Current Format)

```json
{
  "productId": 1,
  "locationId": 59,
  "startDate": "2025-01-01T00:00:00",
  "endDate": "2026-12-31T23:59:59",
  "oltp": {
    "model": "OLTP",
    "runs": 200,
    "averageMillis": 2.916826,
    "result": {
      "totalQuantity": 5,
      "totalRevenue": 13421.10,
      "totalTransactions": 1
    }
  },
  "dimensional": {
    "model": "DIMENSIONAL",
    "runs": 200,
    "averageMillis": 1.132803,
    "result": {
      "totalQuantity": 5,
      "totalRevenue": 13421.10,
      "totalTransactions": 1
    }
  },
  "dimensionalImprovementPercent": 61.16
}
```

This means the dimensional model is about 61% faster for this sample query.

---

## Additional Resources

- [OLTP_DESIGN.md](OLTP_DESIGN.md) - Detailed database design
- [ER_DIAGRAM.md](ER_DIAGRAM.md) - Entity relationship diagram
- [PHASES.md](PHASES.md) - Project development phases
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Hibernate Documentation](https://hibernate.org/)
- [REST API Best Practices](https://restfulapi.net/)

---

## License

This project is for **educational purposes**. Feel free to use, modify, and learn from it.

**Author:** Visura Rodrigo

**Last Updated:** March 23, 2026

---

**Happy coding! 🚀**
