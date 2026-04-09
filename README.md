# OLTP Sales Management System

A comprehensive Spring Boot application demonstrating high-performance inventory and sales management for real-time operations. Built with Sri Lankan business context featuring products, customers, locations, and transactions in LKR currency.

**OLTP** = Online Transaction Processing — optimized for fast, reliable handling of many small operations, just like a real retail store or supermarket.

---

## 🚀 Quick Start

**Windows (Easiest):**
```cmd
.\run-app.bat
```

**All Platforms:**
```bash
mvn clean install
mvn spring-boot:run
```

The app starts at **http://localhost:8080** with 100 pre-loaded sample records.

---

## 📋 What You Can Do

- ✅ **Manage Customers** — Add, edit, search, delete customer records
- ✅ **Real-Time Inventory** — Track stock and get low-stock alerts
- ✅ **Process Sales** — Record transactions with payment tracking
- ✅ **Store Locations** — Manage multiple retail, warehouse, and online stores
- ✅ **Auto SKU Generation** — Products get unique codes automatically
- ✅ **Web Dashboard** — Beautiful responsive UI with purple gradient design
- ✅ **REST API** — Full CRUD operations for programmatic access
- ✅ **Analytics** — Compare OLTP vs Data Warehouse performance
- ✅ **Data Warehouse Pipeline** — Phase 3 incremental ETL with star schema
- ✅ **Sample Data** — 100 pre-loaded records in Sri Lankan context (LKR currency)

---

## ⚡ Key Features

**Web Interface:**
- Intuitive dashboard with real-time statistics
- Product management with search and filtering
- Customer relationship tracking
- Sales transaction history
- Store location management
- Responsive design (desktop, tablet, mobile)

**REST API:**
- Complete CRUD for all entities
- Advanced queries (date range, analytics, etc.)
- Performance benchmarking endpoints
- Warehouse pipeline operations
- Datamart analytics queries

**Data Processing:**
- OLTP layer for transactional workloads
- Dimensional model for analytical queries
- Full warehouse pipeline (staging → star schema → datamart)
- Watermark-based incremental refresh
- Automatic reconciliation and status monitoring

**Architecture:**
- Normalized OLTP database optimized for writes
- Denormalized star schema optimized for reads
- Pre-aggregated datamart for fast analytics
- ACID-compliant transactions
- Audit trail with created/updated timestamps

---

## 📦 Prerequisites

| Requirement | Version | Command to Verify |
|-------------|---------|-------------------|
| **Java** | 21+ (LTS) | `java -version` |
| **Maven** | 3.9+ | `mvn --version` |
| **Port 8080** | Available | `netstat -an \| findstr 8080` |
| **Git** | Any | `git --version` (optional) |

**Install Java 21:**
- Windows: [Oracle Downloads](https://www.oracle.com/java/technologies/downloads/)
- Mac: `brew install java`
- Linux: `sudo apt-get install openjdk-21-jdk`

**Install Maven 3.9+:**
- Windows: `choco install maven` (or [manual download](https://maven.apache.org/download.cgi))
- Mac: `brew install maven`
- Linux: `sudo apt-get install maven`

---

## 🔧 Installation

```bash
# Clone the repository
git clone https://github.com/visurarodrigo/OLTP-Sales-Management-System-.git
cd "OLTP-Sales-Management-System-"

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

**Alternatively on Windows:**
```cmd
.\run-app.bat
```

**Or create a JAR and run:**
```bash
mvn clean package
java -jar target/oltp-0.0.1-SNAPSHOT.jar
```

---

## 🌐 Access Points

| Feature | URL |
|---------|-----|
| **Dashboard** | http://localhost:8080 |
| **Products** | http://localhost:8080/products |
| **Customers** | http://localhost:8080/customers |
| **Locations** | http://localhost:8080/locations |
| **Sales** | http://localhost:8080/sales |
| **H2 Database Console** | http://localhost:8080/h2-console |
| **REST API** | http://localhost:8080/api |

---

## 📁 Project Structure

```
src/main/java/com/oltp/
├── SalesSystemApplication.java         ← Application entry point
├── config/
│   └── DataLoader.java                 ← Pre-loads 100 sample records
├── controller/                         ← Request handlers
│   ├── HomeController.java             ← Dashboard
│   ├── Web*Controller.java             ← Web UI pages (5 controllers)
│   ├── *Controller.java                ← REST APIs (4 controllers)
│   └── BenchmarkController.java        ← Performance benchmarking
├── entity/                             ← Database models (OLTP + Warehouse)
│   ├── Customer, Product, Location, Sales
│   ├── StageSales, DimProduct, DimLocation, DimDate, FactSales
│   ├── SalesDatamartDaily, WarehousePipelineState
├── repository/                         ← Database queries
│   ├── *Repository.java                ← Data access objects (11 repos)
├── service/                            ← Business logic
│   ├── *Service.java                   ← Core services (5 services)
│   └── WarehouseService.java           ← ETL and datamart logic
└── dto/                                ← Data transfer objects (6 DTOs)

src/main/resources/
├── application.properties               ← Database & server config
├── templates/                           ← HTML pages (8 pages)
│   ├── index.html, products.html, customers.html, etc.
└── static/css/
    └── modern-ui.css                   ← Styling (purple gradient theme)

---

## 🗄️ Database Schema

### Core OLTP Tables

#### Customer — Stores customer information

| Field | Type | Notes |
|-------|------|-------|
| `customer_id` | Integer | Auto-generated primary key |
| `first_name`, `last_name` | String | Customer name |
| `email` | String | Indexed for fast search |
| `phone` | String | Contact number |
| `date_of_birth` | Date | Demographics |
| `address`, `city`, `state`, `country`, `postal_code` | String | Full address |
| `customer_status` | Enum | ACTIVE \| INACTIVE \| SUSPENDED |
| `created_at`, `updated_at` | Timestamp | Audit trail |

**Example:** Ravi Jayaraman | ravi.jayaraman@example.lk | Colombo | ACTIVE

---

#### Product — Stores inventory items

| Field | Type | Notes |
|-------|------|-------|
| `product_id` | Integer | Auto-generated primary key |
| `sku` | String | Indexed, auto-generated (e.g., PROD-2026-001) |
| `product_name` | String | Product title |
| `description` | String | Product details |
| `category`, `sub_category` | String | Taxonomy |
| `price` | Decimal | Selling price in LKR |
| `cost_price` | Decimal | Cost to business in LKR |
| `stock_quantity` | Integer | Real-time inventory |
| `reorder_level` | Integer | Low-stock alert threshold |
| `product_status` | Enum | AVAILABLE \| OUT_OF_STOCK \| DISCONTINUED |
| `brand`, `weight` | String | Product attributes |
| `created_at`, `updated_at` | Timestamp | Audit trail |

**Example:** Samsung 27" Monitor | PROD-2026-001 | 45,000 LKR | 42 units in stock

---

#### Location — Stores store/warehouse locations

| Field | Type | Notes |
|-------|------|-------|
| `location_id` | Integer | Auto-generated primary key |
| `store_code` | String | Indexed unique code (e.g., COL-001) |
| `store_name` | String | Location name |
| `location_type` | Enum | RETAIL \| WAREHOUSE \| OUTLET \| ONLINE |
| `address`, `city`, `state`, `country`, `postal_code` | String | Full address in Sri Lanka |
| `phone`, `email` | String | Contact details |
| `manager_name` | String | Store manager |
| `opening_time`, `closing_time` | Time | Operating hours (e.g., 09:00-21:00) |
| `store_capacity` | String | Capacity or square footage |
| `location_status` | Enum | ACTIVE \| INACTIVE \| UNDER_RENOVATION |
| `created_at`, `updated_at` | Timestamp | Audit trail |

**Example:** Colombo Main Store | COL-001 | RETAIL | Manager: Keshan Perera | 09:00-21:00

---

#### Sales — Records all transactions

| Field | Type | Notes |
|-------|------|-------|
| `sale_id` | Integer | Auto-generated primary key |
| `order_number` | String | Unique order ID (e.g., ORD-2026-00001) |
| `customer_id` | Integer | Foreign key to Customer |
| `product_id` | Integer | Foreign key to Product |
| `location_id` | Integer | Foreign key to Location |
| `quantity` | Integer | Units sold |
| `unit_price` | Decimal | Price per unit (LKR) |
| `subtotal` | Decimal | quantity × unit_price |
| `discount_amount` | Decimal | Discount (LKR) |
| `tax_amount` | Decimal | Tax (LKR) |
| `total_amount` | Decimal | Final amount paid (LKR) |
| `payment_method` | Enum | CASH \| CREDIT_CARD \| DEBIT_CARD \| DIGITAL_WALLET |
| `payment_status` | Enum | PAID \| PENDING \| REFUNDED \| FAILED |
| `order_status` | Enum | COMPLETED \| PROCESSING \| CANCELLED \| RETURNED |
| `sale_date` | DateTime | Indexed for date queries |
| `delivery_date` | DateTime | Delivery timestamp |
| `notes` | Text | Additional notes |
| `created_at`, `updated_at` | Timestamp | Audit trail |

**Example:** ORD-2026-00001 | Ravi Jayaraman | Samsung Monitor | 1 × 45,000 LKR | Total: 50,200 LKR | PAID

### Warehouse Tables (Phase 3)

| Table | Purpose |
|-------|---------|
| **stage_sales** | Staging layer for OLTP sales extraction |
| **dim_product** | Product dimension (slowly changing) |
| **dim_location** | Location dimension (slowly changing) |
| **dim_date** | Date dimension (all dates for fast queries) |
| **fact_sales** | Denormalized facts optimized for aggregation |
| **sales_datamart_daily** | Pre-aggregated daily sales (quantity, revenue, count) |
| **warehouse_pipeline_state** | Watermark tracking for incremental refresh |

---

## 🛠️ Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| Java | 21 (LTS) | Programming language |
| Spring Boot | 3.5.0 | Web framework & embedded server |
| Spring Data JPA | 3.5.0 | Database ORM mapping |
| Spring Web MVC | 3.5.0 | REST APIs & web controllers |
| Thymeleaf | 3.1.x | Server-side HTML templates |
| Hibernate | 6.4.x | JPA implementation for SQL generation |
| H2 Database | 2.2.x | In-memory DB (dev/testing) |
| Lombok | 1.18.x | Boilerplate code generation |
| Jackson | 2.15.x | JSON serialization |
| Maven | 3.9+ | Build & dependency management |

---

## 📊 Web Dashboard

The application includes a modern, responsive web UI.

**Pages:**
- **Dashboard** (http://localhost:8080) — Overview with key metrics
- **Products** (http://localhost:8080/products) — List, search, add, edit products
- **Customers** (http://localhost:8080/customers) — Manage customer relationships
- **Locations** (http://localhost:8080/locations) — View store locations as cards
- **Sales** (http://localhost:8080/sales) — Track transactions & revenue

**Features:**
- Real-time search and filtering
- Add/Edit/Delete forms with validation
- Stock-level warnings
- Revenue calculations
- Responsive design (mobile-friendly)
- Purple gradient UI with smooth animations
- Confirmation dialogs for destructive actions
- Status badges for quick scanning

---

## 🔌 REST API

Base URL: `http://localhost:8080/api`

### Customer API

```bash
GET    /api/customers                      # List all
GET    /api/customers/{id}                 # Get one
GET    /api/customers/email/{email}        # Search by email
POST   /api/customers                      # Create
PUT    /api/customers/{id}                 # Update
DELETE /api/customers/{id}                 # Delete
```

### Product API

```bash
GET    /api/products                       # List all
GET    /api/products/{id}                  # Get one
GET    /api/products/sku/{sku}             # Search by SKU
GET    /api/products/category/{category}   # Filter by category
GET    /api/products/reorder               # Low stock alerts
POST   /api/products                       # Create
PUT    /api/products/{id}                  # Update
PUT    /api/products/{id}/stock?quantity={n} # Update stock
DELETE /api/products/{id}                  # Delete
```

### Location API

```bash
GET    /api/locations                      # List all
GET    /api/locations/{id}                 # Get one
GET    /api/locations/active               # Active stores only
POST   /api/locations                      # Create
PUT    /api/locations/{id}                 # Update
DELETE /api/locations/{id}                 # Delete
```

### Sales API

```bash
GET    /api/sales                          # List all
GET    /api/sales/{id}                     # Get one
GET    /api/sales/date-range?startDate=...&endDate=...
       # Date range query
GET    /api/sales/product-location?productId={id}&locationId={id}&startDate=...&endDate=...
       # Get transactions for product at location
GET    /api/sales/analytics/product-location?productId={id}&locationId={id}&startDate=...&endDate=...
       # Aggregated analytics (quantity, revenue, count)
POST   /api/sales                          # Create sale
PUT    /api/sales/{id}                     # Update
DELETE /api/sales/{id}                     # Delete
```

### Warehouse & Benchmark API

```bash
GET/POST /api/benchmark/warehouse/rebuild         # Full ETL pipeline
GET/POST /api/benchmark/warehouse/incremental/run # Incremental refresh
GET      /api/benchmark/warehouse/status          # Pipeline status
GET      /api/benchmark/warehouse/reconcile?startDate=...&endDate=...
         # Validate data integrity
GET      /api/benchmark/datamart/daily?startDate=...&endDate=...
         # Daily analytics
GET      /api/benchmark/datamart/top-products?limit=10
         # Top selling products
GET      /api/benchmark/datamart/top-locations?limit=10
         # Top performing locations
GET      /api/benchmark/sales-compare?productId={id}&locationId={id}&startDate=...&endDate=...&runs=30
         # Compare OLTP vs Dimensional performance
```

**Example Request:**
```bash
curl -X GET "http://localhost:8080/api/customers" \
  -H "Content-Type: application/json"
```

**Example Response:**
```json
[
  {
    "customerId": 1,
    "firstName": "Ravi",
    "lastName": "Jayaraman",
    "email": "ravi.jayaraman@example.lk",
    "phone": "+94-11-2345678",
    "city": "Colombo",
    "customerStatus": "ACTIVE"
  }
]
```

---

## 📦 Sample Data

The application auto-loads 100 realistic records on startup:

**Customers (Sri Lankan Names & Context):**
- Ravi Jayaraman, Sasha Perera, Dilkshan Silva, Priya Mendis, etc.
- Cities: Colombo, Kandy, Galle, Jaffna, Matara, Batticaloa
- LKR-based pricing

**Products:**
- Electronics (monitors, keyboards, mice)
- Furniture (chairs, desks, tables)
- Office Supplies (stationery)
- Food & Beverages
- Clothing & Accessories
- Prices in LKR with realistic margins

**Locations:**
- Colombo Main Store (Retail)
- Kandy Branch (Retail)
- Galle Outlet (Outlet)
- Central Warehouse (Warehouse)
- Online Store (Online Channel)

**Sales:**
- 100 realistic transactions
- Mixed customers, products, locations
- Various payment methods
- Distributed across March 2026

---

## 🏗️ OLTP Design Principles

This system demonstrates best practices for transaction processing:

### 1. Normalization (3NF)
- Customer data stored once, referenced by ID in Sales
- No data duplication → consistency & storage efficiency
- Updates happen in one place → instant across system

### 2. Indexing Strategy
- Primary Keys: `customer_id`, `product_id`, `sale_id` (instant lookup)
- Email: Fast customer search
- SKU: Product search at checkout
- sale_date: Quick date range reports
- 10-100x faster queries with proper indexes

### 3. Foreign Key Constraints
- Sales must reference valid customers, products, locations
- Database prevents orphaned records
- Automatic referential integrity enforcement

### 4. ACID Transactions  
- **Atomicity**: Sale fully succeeds or fully rolls back
- **Consistency**: Stock + sales always match
- **Isolation**: Concurrent checkouts don't interfere
- **Durability**: Saved data survives crashes

### 5. Audit Trail
- `created_at` automatic timestamp at insert
- `updated_at` automatic timestamp at every update
- Track changes, debug issues, compliance logging

### 6. Real-Time Operations
- Stock updates immediately on sale
- Dashboard shows live numbers
- No lag or delay
- Prevents overselling

---

## ⚙️ Configuration

**Main Config File:** `src/main/resources/application.properties`

```properties
# Server
server.port=8080
server.servlet.context-path=

# Database (H2 - In Memory, perfect for dev/testing)
spring.datasource.url=jdbc:h2:mem:oltp_sales_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop  # Auto-create tables
spring.jpa.show-sql=false

# H2 Web Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Switch to MySQL

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/oltp_sales_db
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

Add to `pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
</dependency>
```

### Switch to PostgreSQL

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/oltp_sales_db
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.0</version>
</dependency>
```

---

## 🐛 Troubleshooting

| Problem | Cause | Solution |
|---------|-------|----------|
| **Port 8080 already in use** | Another app on port 8080 | Change `server.port=8081` in application.properties |
| **Java version error** | Java 21 not installed | `java -version` should show 21.x.x |
| **Maven not found** | Maven not in PATH | Install Maven or add to PATH environment variable |
| **App won't start** | Build failed | Check console for errors: `mvn spring-boot:run 2>&1` |
| **Can't access http://localhost:8080** | App not running or firewall blocked | Wait 5-10 seconds for startup, check firewall |
| **No sample data** | DataLoader didn't run | Check H2 console: http://localhost:8080/h2-console |
| **Database errors** | H2 syntax issue | Verify `application.properties` settings |

**Check Port Availability:**
```bash
# Windows
netstat -an | findstr 8080

# Mac/Linux
lsof -i :8080
```

**View Application Logs:**
```bash
mvn spring-boot:run | grep -E "ERROR|Exception"
```

---

## 🏭 Data Warehouse Pipeline (Phase 3)

The system includes a complete warehouse architecture for analytics:

### Architecture Layers

**OLTP Layer:**
- Optimized for writes (transactional)
- Normalized to reduce storage
- Many indexed columns for lookup speed
- Example: Customer updates trigger immediate sales changes

**Staging Layer:**
- Raw extraction from OLTP = `stage_sales`
- Controlled ETL input source
- Watermark tracking for incremental sync

**Warehouse Layer (Star Schema):**
- Denormalized for read speed
- Reduces query complexity
- `dim_product` — Product catalog
- `dim_location` — Store/location details
- `dim_date` — Pre-calculated dates (year, month, day of week, etc.)
- `fact_sales` — Quantified facts (quantity, amount)

**Datamart:**
- `sales_datamart_daily` — Pre-aggregated daily totals
- Used for dashboards and quick reports
- Computed once per day from facts

### Warehouse Operations

**Full Pipeline Rebuild:**
```bash
curl -X GET "http://localhost:8080/api/benchmark/warehouse/rebuild"
```
Runs: OLTP → Staging → Star Schema → Datamart (full refresh)

**Incremental Refresh:**
```bash
curl -X GET "http://localhost:8080/api/benchmark/warehouse/incremental/run"
```
Runs: Extracts new OLTP changes → Staging → Updates Star Schema → Refreshes Datamart

**Check Status:**
```bash
curl -X GET "http://localhost:8080/api/benchmark/warehouse/status"
```
Returns pipeline status, last update time, and record counts

**Reconciliation:**
```bash
curl -X GET "http://localhost:8080/api/benchmark/warehouse/reconcile?startDate=2026-03-01&endDate=2026-03-31"
```
Validates OLTP totals match Warehouse totals

### Performance Comparison

**Run Benchmark:**
```bash
curl "http://localhost:8080/api/benchmark/sales-compare?productId=1&locationId=1&startDate=2026-03-01T00:00:00&endDate=2026-03-31T23:59:59&runs=30"
```

**Response:**
```json
{
  "oltp": {
    "model": "OLTP",
    "averageMillis": 2.92,
    "result": { "totalQuantity": 5, "totalRevenue": 13421.10 }
  },
  "dimensional": {
    "model": "DIMENSIONAL",
    "averageMillis": 1.13,
    "result": { "totalQuantity": 5, "totalRevenue": 13421.10 }
  },
  "dimensionalImprovementPercent": 61.16
}
```

The dimensional model is ~61% faster for this query due to pre-aggregation and denormalization.

---

## 📖 Additional Resources

- [OLTP_DESIGN.md](OLTP_DESIGN.md) — Detailed design documentation
- [ER_DIAGRAM.md](ER_DIAGRAM.md) — Entity relationship diagrams
- [PHASES.md](PHASES.md) — Project development phases & milestones
- [H2 Database Documentation](https://www.h2database.com/)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Hibernate ORM Guide](https://hibernate.org/)
- [REST API Best Practices](https://restfulapi.net/)

---

## ℹ️ Project Information

| Property | Value |
|----------|-------|
| **Repository** | [OLTP-Sales-Management-System](https://github.com/visurarodrigo/OLTP-Sales-Management-System-) |
| **Branch** | `main` (stable) |
| **License** | Educational & Open Source |
| **Author** | Visura Rodrigo |
| **Last Updated** | April 10, 2026 |

**Clone the Project:**
```bash
git clone https://github.com/visurarodrigo/OLTP-Sales-Management-System-.git
cd "OLTP-Sales-Management-System-"
```

---

## 📝 License

This project is for **educational purposes**. Feel free to use, modify, learn, and contribute.


