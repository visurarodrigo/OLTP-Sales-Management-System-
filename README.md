# OLTP Sales Management System

A **Spring Boot** application demonstrating an **Online Transaction Processing (OLTP)** database system for sales management, optimized for high-frequency transactional operations including real-time sales processing, inventory management, customer tracking, and location-based operations.

## Features

### 🎨 Web User Interface
- **Dashboard** - Real-time statistics and quick navigation
- **Product Management** - Full CRUD operations with search functionality
- **Customer Management** - Add, edit, delete customers with search
- **Location Viewer** - Display store locations in card layout
- **Sales Transactions** - View all sales with revenue calculation
- **Auto-SKU Generation** - Automatically generates SKU codes for products
- **Responsive Design** - Modern gradient UI with smooth animations

### 🔌 REST API
- Complete RESTful API for all entities
- JSON-based data exchange
- Support for CRUD operations
- Transaction management

## Quick Start

### Prerequisites
- **Java 8 or higher** (JDK 8, 11, 17, or 21)
- **Maven 3.6+** installed and configured
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

- **Java 8** (Compatible with Java 8, 11, 17, 21)
- **Spring Boot 2.7.18**
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

Verify installation:
```bash
java -version
```

### 2. Install Maven

**Windows (Using Chocolatey):**
```powershell
choco install maven -y
```

**Manual Installation:**
1. Download from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Add `MAVEN_HOME` and update `PATH` environment variables

**Verify installation:**
```bash
mvn --version
```

### 3. Configure Java Home (if needed)

**Windows:**
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202
```

### 4. Build and Run

Clone/download the project, then:
```bash
cd oltp-sales-system
mvn clean install
mvn spring-boot:run
```

Or simply run:
```cmd
.\run-app.bat
```

## Sample Data

The application automatically loads sample data on startup:
- **100 Customers** across different US locations
- **100 Products** across multiple categories
- **100 Locations** (retail, outlet, warehouse, and online)
- **100 Sales Transactions** with various statuses

## Web User Interface

The application provides a modern, user-friendly web interface built with Thymeleaf templates and styled with CSS gradients.

### Dashboard (/)
- **Statistics Cards**: Display total products, customers, sales, and locations
- **Quick Navigation**: Cards linking to each management section
- **Modern Design**: Purple gradient background with smooth animations

### Product Management (/products)
- **Product List**: View all products in a table format
- **Search Functionality**: Real-time search by product name or SKU
- **Add New Product**: Form with auto-SKU generation
- **Edit Product**: Update existing product details
- **Delete Product**: Remove products with confirmation
- **Product Details**: View individual product information
- **Stock Indicators**: Visual badges for low stock warnings

### Customer Management (/customers)
- **Customer List**: View all customers in a table
- **Search by Name/Email**: Real-time search functionality
- **Add New Customer**: Create customer with all details
- **Edit Customer**: Update customer information
- **Delete Customer**: Remove customers with confirmation

### Location Viewer (/locations)
- **Card Layout**: Display stores in visually appealing cards
- **Location Details**: Store name, city, state, country, postal code
- **Clean Design**: Easy to scan location information

### Sales Transactions (/sales)
- **Transaction Table**: View all sales with full details
- **Summary Cards**: Total transactions and revenue
- **Revenue Calculation**: Automatic total revenue display
- **Transaction Details**: Customer, product, quantity, price, location

### Features
- ✅ **Responsive Design**: Works on desktop and mobile
- ✅ **Modern UI**: Gradient backgrounds and smooth transitions
- ✅ **Real-time Search**: Instant filtering on products and customers
- ✅ **Form Validation**: Required field validation
- ✅ **Auto-SKU Generation**: Automatically creates SKU codes
- ✅ **Stock Status Badges**: Visual indicators for inventory levels
- ✅ **Confirmation Dialogs**: Prevents accidental deletions

## REST API Endpoints

### Customers
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| POST | `/api/customers` | Create new customer |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create new product |
| PUT | `/api/products/{id}` | Update product |
| PUT | `/api/products/{id}/stock?quantity=X` | Update stock quantity |
| DELETE | `/api/products/{id}` | Delete product |

### Locations
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/locations` | Get all locations |
| GET | `/api/locations/{id}` | Get location by ID |
| POST | `/api/locations` | Create new location |
| PUT | `/api/locations/{id}` | Update location |
| DELETE | `/api/locations/{id}` | Delete location |

### Sales
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/sales` | Get all sales |
| GET | `/api/sales/{id}` | Get sale by ID |
| GET | `/api/sales/date-range?startDate=...&endDate=...` | Get sales by date range |
| GET | `/api/sales/product-location?productId=...&locationId=...&startDate=...&endDate=...` | Get sales for a product in a location over a period |
| GET | `/api/sales/analytics/product-location?productId=...&locationId=...&startDate=...&endDate=...` | Get analytics (totals + maximum sales stats) for that filter |
| POST | `/api/sales` | Create new sale |
| PUT | `/api/sales/{id}` | Update sale |
| DELETE | `/api/sales/{id}` | Delete sale |

### Benchmark and Phase 2 (Dimensional Model)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/benchmark/warehouse/rebuild` | Rebuilds dimension and fact tables from OLTP data |
| GET | `/api/benchmark/sales-compare?productId=...&locationId=...&startDate=...&endDate=...&runs=20` | Compares query performance of OLTP vs dimensional model |

### Example API Usage

**Get all customers:**
```bash
curl http://localhost:8080/api/customers
```

**Create a new customer:**
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "555-1234",
    "customerStatus": "ACTIVE"
  }'
```
| `GET /api/customers/email/{email}` | Customer by email |
| `GET /api/products` | All products |
| `GET /api/products/sku/{sku}` | Product by SKU |
| `GET /api/products/category/{category}` | Products by category |
| `GET /api/products/reorder` | Products needing restock |
| `GET /api/locations` | All locations |
| `GET /api/locations/active` | Active locations |
| `GET /api/sales` | All sales |
| `GET /api/sales/customer/{id}` | Sales by customer |
| `GET /api/sales/product-location?...` | Sales by product + location + date range |
| `GET /api/sales/analytics/product-location?...` | Max/total sales analytics for product + location + date range |
| `POST /api/*` | Create new record |

## Example API Usage

```bash
# Get all customers
curl http://localhost:8080/api/customers

# Get products needing reorder
curl http://localhost:8080/api/products/reorder
```

## Switching to MySQL/PostgreSQL

Update `application.properties`:

**MySQL:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/oltp_sales_db
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

**PostgreSQL:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/oltp_sales_db
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

Add respective database driver to `pom.xml`.

## OLTP Features

- ✅ **Normalized to 3NF** - Eliminates data redundancy
- ✅ **Strategic Indexing** - Fast lookups on email, SKU, dates, status
- ✅ **Foreign Key Constraints** - Referential integrity
- ✅ **ACID Transactions** - Guaranteed consistency
- ✅ **Audit Timestamps** - created_at/updated_at on all tables
- ✅ **Real-time Processing** - Immediate stock updates

See [OLTP_DESIGN.md](OLTP_DESIGN.md) for detailed design documentation.

## Phase 2: Dimensional Model (Fact + Dimensions)

This project now includes a simple star schema for analytics:
- `dim_product`
- `dim_location`
- `dim_date`
- `fact_sales`

These are populated automatically from OLTP tables at startup.

Use this API to compare query performance between models:

```bash
curl "http://localhost:8080/api/benchmark/sales-compare?productId=1&locationId=1&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59&runs=30"
```

Response includes:
- aggregated result (quantity, revenue, transaction count)
- average execution time for OLTP query
- average execution time for dimensional query
- dimensional improvement percentage

---

**License:** Educational purposes
