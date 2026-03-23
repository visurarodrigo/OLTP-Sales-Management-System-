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

- JDK 21
- Maven 3.9+
- Port 8080 available

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
