# OLTP Sales Management System

A Spring Boot application for Online Transaction Processing (OLTP) in sales management.

It handles day-to-day operations such as:

- customer management
- product and inventory tracking
- location management
- sales transaction processing
- analytics and benchmark comparison

## Current Version

- Java 21 (LTS)
- Spring Boot 3.5.0
- Jakarta Persistence (`jakarta.persistence.*`)
- Maven 3.9+
- H2 in-memory database (default)

---

## Features

### Web User Interface

- Dashboard with quick statistics
- Product management (add, view, edit in web flow, delete)
- Customer management (add, view, edit in web flow, delete)
- Location browsing
- Sales transactions view with totals
- Responsive UI for desktop and mobile

### REST API

- CRUD-style endpoints for customers, products, locations, and sales
- Filter endpoints by status/category/date range
- Revenue and product-location analytics
- OLTP vs dimensional-model benchmark endpoints

### Data and Benchmarking

- OLTP transactional model (`customer`, `product`, `location`, `sales`)
- Dimensional model (`dim_product`, `dim_location`, `dim_date`, `fact_sales`)
- Performance comparison API for analytical queries

---

## Technology Stack

- Java 21 (LTS)
- Spring Boot 3.5.0
- Spring Data JPA
- Spring Web MVC
- Thymeleaf
- Hibernate
- H2 Database
- Lombok
- Maven

---

## Quick Start

### Prerequisites
<<<<<<< HEAD

- JDK 21
- Maven 3.9+
- Port 8080 available
=======
- **Java 21 (LTS)**
- **Maven 3.9+** installed and configured
- **Port 8080** available

### Running the Application

**Windows:**
```cmd
.\run-app.bat
```

**Linux/Mac or Manual:**
```bash
mvn spring-boot:run
```

**Access Points:**
- **Dashboard:** http://localhost:8080 (Main web interface)
- **Products:** http://localhost:8080/products (Product management)
- **Customers:** http://localhost:8080/customers (Customer management)
- **Locations:** http://localhost:8080/locations (Store locations)
- **Sales:** http://localhost:8080/sales (Sales transactions)
- **H2 Console:** http://localhost:8080/h2-console (Database viewer)
  - JDBC URL: `jdbc:h2:mem:oltp_sales_db`
  - Username: `sa`
  - Password: _(leave blank)_
- **REST API Base:** http://localhost:8080/api/

### Stopping the Application
Press `Ctrl+C` in the terminal

## Database Schema

### Tables and OLTP-Optimized Attributes

#### 1. **Customer Table**
Stores customer information with attributes optimized for quick lookups and transactional processing.

**Attributes:**
- `customer_id` (PK) - Auto-incrementing primary key for fast indexing
- `first_name`, `last_name` - Customer name
- `email` - Unique identifier, indexed for quick searches
- `phone` - Indexed for contact lookups
- `date_of_birth` - Customer demographics
- `address`, `city`, `state`, `country`, `postal_code` - Location details
- `customer_status` - ACTIVE, INACTIVE, SUSPENDED (for business logic)
- `created_at`, `updated_at` - Audit timestamps

**OLTP Features:**
- Indexed email and phone for fast customer lookup during checkout
- Status field enables quick filtering of active customers
- Normalized structure reduces redundancy
- Timestamps for audit trail

#### 2. **Product Table**
Manages product catalog with real-time inventory tracking.

**Attributes:**
- `product_id` (PK) - Primary key
- `sku` - Stock Keeping Unit, unique identifier indexed
- `product_name`, `description` - Product details
- `category`, `sub_category` - Indexed for filtering
- `price`, `cost_price` - Pricing information
- `stock_quantity` - Real-time inventory count
- `reorder_level` - Threshold for restocking alerts
- `product_status` - AVAILABLE, OUT_OF_STOCK, DISCONTINUED
- `brand`, `weight` - Additional product attributes
- `created_at`, `updated_at` - Audit timestamps

**OLTP Features:**
- SKU indexed for quick product lookups at POS
- Real-time stock tracking for inventory management
- Category indexing enables fast product filtering
- Reorder level supports automated inventory alerts

#### 3. **Location Table**
Tracks physical stores, warehouses, and online channels.

**Attributes:**
- `location_id` (PK) - Primary key
- `store_code` - Unique store identifier, indexed
- `store_name` - Location name
- `location_type` - RETAIL, WAREHOUSE, OUTLET, ONLINE
- `address`, `city`, `state`, `country`, `postal_code` - Geographic data
- `phone`, `email` - Contact information
- `manager_name` - Store manager
- `opening_time`, `closing_time` - Operating hours
- `store_capacity` - Physical capacity or square footage
- `location_status` - ACTIVE, INACTIVE, UNDER_RENOVATION
- `created_at`, `updated_at` - Audit timestamps

**OLTP Features:**
- Store code indexing for fast location lookups
- Location type enables channel-based reporting
- Status field for operational filtering
- Geographic indexing for regional queries

#### 4. **Sales Table**
Records individual sales transactions with complete transaction details.

**Attributes:**
- `sale_id` (PK) - Primary key
- `order_number` - Unique order identifier
- `customer_id` (FK) - References Customer table
- `product_id` (FK) - References Product table
- `location_id` (FK) - References Location table
- `quantity` - Items purchased
- `unit_price`, `subtotal` - Pricing breakdown
- `discount_amount`, `tax_amount` - Financial calculations
- `total_amount` - Final transaction amount
- `payment_method` - CASH, CREDIT_CARD, DEBIT_CARD, DIGITAL_WALLET
- `payment_status` - PAID, PENDING, REFUNDED, FAILED
- `order_status` - COMPLETED, PROCESSING, CANCELLED, RETURNED
- `sale_date` - Transaction timestamp, heavily indexed
- `delivery_date` - Fulfillment date
- `notes` - Additional transaction notes
- `created_at`, `updated_at` - Audit timestamps

**OLTP Features:**
- Multiple indexes (date, customer, product, location) for fast queries
- Foreign key constraints ensure referential integrity
- Status fields enable workflow management
- Sale date indexing optimizes time-based reporting
- Transaction-level detail supports financial reconciliation

## OLTP Design Principles Applied

### 1. **Normalization**
- Tables are normalized to 3NF to reduce data redundancy
- Foreign key relationships maintain data integrity
- No duplicate customer or product information

### 2. **Indexing Strategy**
- Primary keys on all ID columns
- Secondary indexes on frequently queried columns (email, SKU, store_code, sale_date)
- Composite indexes where needed for multi-column queries
- Index on status fields for filtering

### 3. **Data Integrity**
- Foreign key constraints enforce referential integrity
- Unique constraints on business keys (email, SKU, store_code, order_number)
- NOT NULL constraints on critical fields
- Cascade rules for related data management

### 4. **Audit Trail**
- `created_at` and `updated_at` timestamps on all tables
- Automatic timestamp management via JPA annotations
- Historical tracking capability

### 5. **Transaction Support**
- JPA/Hibernate provides ACID transaction support
- Service layer methods are transactional
- Stock updates are atomic with sales creation

### 6. **Real-time Operations**
- Immediate stock quantity updates
- Real-time customer and order status changes
- Current inventory visibility

## Technology Stack

- **Java 21 (LTS)**
- **Spring Boot 3.5.0**
- **Spring Data JPA** - Data access layer
- **Spring Web MVC** - Web layer and REST API
- **Thymeleaf** - Server-side template engine for web UI
- **Hibernate** - ORM framework
- **H2 Database** - In-memory database (easily switchable to MySQL/PostgreSQL)
- **Lombok** - Reduces boilerplate code
- **Jackson** - JSON serialization
- **Maven** - Build and dependency management

## Project Structure

```
oltp-sales-system/
├── src/
│   └── main/
│       ├── java/com/oltp/
│       │   ├── SalesSystemApplication.java    # Main application entry point
│       │   ├── config/
│       │   │   └── DataLoader.java            # Sample data initialization
│       │   ├── controller/
│       │   │   ├── HomeController.java        # Dashboard web page
│       │   │   ├── WebProductController.java  # Product web UI controller
│       │   │   ├── WebCustomerController.java # Customer web UI controller
│       │   │   ├── WebLocationController.java # Location web UI controller
│       │   │   ├── WebSalesController.java    # Sales web UI controller
│       │   │   ├── CustomerController.java    # Customer REST API
│       │   │   ├── ProductController.java     # Product REST API
│       │   │   ├── LocationController.java    # Location REST API
│       │   │   └── SalesController.java       # Sales REST API
│       │   ├── entity/
│       │   │   ├── Customer.java              # Customer entity
│       │   │   ├── Product.java               # Product entity
│       │   │   ├── Location.java              # Location entity
│       │   │   └── Sales.java                 # Sales entity
│       │   ├── repository/
│       │   │   ├── CustomerRepository.java    # Customer data access
│       │   │   ├── ProductRepository.java     # Product data access
│       │   │   ├── LocationRepository.java    # Location data access
│       │   │   └── SalesRepository.java       # Sales data access
│       │   └── service/
│       │       ├── CustomerService.java       # Customer business logic
│       │       ├── ProductService.java        # Product business logic
│       │       ├── LocationService.java       # Location business logic
│       │       └── SalesService.java          # Sales business logic
│       └── resources/
│           ├── templates/                     # Thymeleaf HTML templates
│           │   ├── index.html                 # Dashboard page
│           │   ├── products.html              # Product list page
│           │   ├── product-form.html          # Add/Edit product form
│           │   ├── product-details.html       # Product details page
│           │   ├── customers.html             # Customer list page
│           │   ├── customer-form.html         # Add/Edit customer form
│           │   ├── locations.html             # Locations display page
│           │   └── sales.html                 # Sales transactions page
│           └── application.properties         # Application configuration
├── pom.xml                                    # Maven dependencies
├── run-app.bat                                # Windows startup script
├── .gitignore                                 # Git ignore rules
├── README.md                                  # This file
├── OLTP_DESIGN.md                             # Detailed design documentation
└── ER_DIAGRAM.md                              # Entity relationship diagram
```
## Setup Instructions

### 1. Install Java
Download and install JDK 8 or higher from:
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- [OpenJDK](https://adoptium.net/)
>>>>>>> da18db105b3f25897f504db7be76316d7a352194

Verify installation:

```bash
java -version
mvn -version
```

### Run the Application

Windows:

```cmd
run-app.bat
```

Any platform:

```bash
mvn spring-boot:run
```

### Access Points

- Dashboard: http://localhost:8080/
- Products: http://localhost:8080/products
- Customers: http://localhost:8080/customers
- Locations: http://localhost:8080/locations
- Sales: http://localhost:8080/sales
- H2 Console: http://localhost:8080/h2-console
- REST API Base: http://localhost:8080/api

H2 Console login:

- JDBC URL: `jdbc:h2:mem:oltp_sales_db`
- Username: `sa`
- Password: leave blank

Stop application: `Ctrl + C`

---

## Build and Test

Build package:

```bash
mvn clean package
```

Run tests:

```bash
mvn test
```

---

## Configuration

Main config file:

- `src/main/resources/application.properties`

Current important defaults:

- `server.port=8080`
- `spring.datasource.url=jdbc:h2:mem:oltp_sales_db`
- `spring.jpa.hibernate.ddl-auto=create-drop`
- H2 console enabled

---

## Database Schema (OLTP)

### Customer

Main fields:

- `customer_id` (PK)
- `first_name`, `last_name`
- `email` (unique/indexed)
- `phone` (indexed)
- `customer_status`
- `created_at`, `updated_at`

### Product

Main fields:

- `product_id` (PK)
- `sku` (unique/indexed)
- `product_name`, `description`
- `category`, `sub_category`
- `price`, `cost_price`
- `stock_quantity`, `reorder_level`
- `product_status`

### Location

Main fields:

- `location_id` (PK)
- `store_code` (unique/indexed)
- `store_name`, `location_type`
- address and contact details
- `location_status`

### Sales

Main fields:

- `sale_id` (PK)
- `order_number` (unique)
- `customer_id`, `product_id`, `location_id` (FK)
- `quantity`, `unit_price`, `subtotal`, `total_amount`
- `payment_status`, `order_status`
- `sale_date`, `delivery_date`

---

## OLTP Design Principles

- Normalization (3NF)
- Primary and secondary indexing
- Foreign key constraints for integrity
- ACID transactions via Spring + JPA
- Audit timestamps (`created_at`, `updated_at`)

---

## REST API Endpoints

### Customers

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| GET | `/api/customers/email/{email}` | Get customer by email |
| GET | `/api/customers/status/{status}` | Get customers by status |
| POST | `/api/customers` | Create customer |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/sku/{sku}` | Get product by SKU |
| GET | `/api/products/category/{category}` | Get products by category |
| GET | `/api/products/reorder` | Get products needing reorder |
| POST | `/api/products` | Create product |
| DELETE | `/api/products/{id}` | Delete product |

### Locations

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/locations` | Get all locations |
| GET | `/api/locations/{id}` | Get location by ID |
| GET | `/api/locations/store-code/{storeCode}` | Get location by store code |
| GET | `/api/locations/type/{type}` | Get locations by type |
| GET | `/api/locations/active` | Get active locations |
| POST | `/api/locations` | Create location |
| DELETE | `/api/locations/{id}` | Delete location |

### Sales

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/sales` | Get all sales |
| GET | `/api/sales/{id}` | Get sale by ID |
| GET | `/api/sales/customer/{customerId}` | Get sales by customer |
| GET | `/api/sales/date-range` | Get sales by date range |
| GET | `/api/sales/product-location` | Get sales by product + location + date range |
| GET | `/api/sales/analytics/product-location` | Get sales analytics by product + location + date range |
| GET | `/api/sales/revenue` | Get revenue in date range |
| POST | `/api/sales` | Create sale |
| DELETE | `/api/sales/{id}` | Delete sale |

### Benchmark

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/benchmark/warehouse/rebuild` | Rebuild dimensional tables from OLTP data |
| GET | `/api/benchmark/sales-compare` | Compare OLTP vs dimensional query performance |

---

## Example API Usage

Get all customers:

```bash
curl http://localhost:8080/api/customers
```

Get products needing reorder:

```bash
curl http://localhost:8080/api/products/reorder
```

Run benchmark comparison:

```bash
curl "http://localhost:8080/api/benchmark/sales-compare?productId=1&locationId=1&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59&runs=30"
```

---

## Dimensional Model (Phase 2)

Analytics model includes:

- `dim_product`
- `dim_location`
- `dim_date`
- `fact_sales`

Warehouse data can be rebuilt from OLTP using benchmark rebuild API.

---

## Switching to MySQL or PostgreSQL

Update datasource settings in `application.properties` and add the relevant JDBC driver in `pom.xml`.

MySQL example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/oltp_sales_db
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

PostgreSQL example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/oltp_sales_db
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

---

## Project Structure

```text
src/main/java/com/oltp/
  config/       startup config and data loading
  controller/   web and REST controllers
  dto/          API request/response models
  entity/       OLTP entities + dimensional entities
  repository/   Spring Data JPA repositories
  service/      business logic and query processing

src/main/resources/
  templates/    Thymeleaf HTML templates
  static/css/   CSS assets
  application.properties
```

---

## Sample Data

On startup, sample Sri Lankan-context data is loaded for demo/testing:

- customers
- products
- locations
- sales records

---

## Troubleshooting

1. Java version issue:
- Ensure `java -version` shows Java 21

2. Maven issue:
- Ensure `mvn -version` works and is Maven 3.9+

3. Port conflict:
- Change `server.port` in `application.properties`

4. H2 login issue:
- Use URL `jdbc:h2:mem:oltp_sales_db`, user `sa`, blank password

---

## Additional Documentation

- `OLTP_DESIGN.md` for detailed OLTP design
- `ER_DIAGRAM.md` for entity relationships
- Folder-level README files inside `src/` for quick navigation

---

## License

Educational purpose project.
