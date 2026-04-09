# OLTP Sales Management System

A Spring Boot application for high performance sales and inventory operations.

This project demonstrates Online Transaction Processing (OLTP) concepts through a practical retail-style system with Sri Lankan sample data and LKR pricing.

## Overview

The system supports day-to-day transactional workloads:
- Customer management
- Product and stock management
- Multi-location sales processing
- Real-time updates for operational use

It also includes an analytics pipeline that compares OLTP queries against a dimensional warehouse model.

## Key Capabilities

### Operational Features
- Customer CRUD with search and status tracking
- Product CRUD with automatic SKU generation
- Stock monitoring with reorder alerts
- Sales processing with payment and order status fields
- Location management for retail, warehouse, outlet, and online channels

### Analytics and Benchmarking
- Warehouse pipeline with full rebuild and incremental refresh
- Star schema and datamart support
- Reconciliation endpoints for data quality checks
- OLTP vs dimensional query performance benchmarking

### Developer Experience
- Ready-to-run with in-memory H2 database
- 100 preloaded records in Sri Lankan business context
- REST APIs and Thymeleaf web UI
- Maven build with Spring Boot runtime

## Tech Stack

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- Spring Web MVC
- Thymeleaf
- Hibernate
- H2 Database
- Lombok
- Maven

## Prerequisites

- Java 21+
- Maven 3.9+
- Port 8080 available

Verification commands:

```bash
java -version
mvn --version
```

Windows port check:

```cmd
netstat -an | findstr 8080
```

## Quick Start

### Windows

```cmd
.\run-app.bat
```

### All Platforms

```bash
mvn clean install
mvn spring-boot:run
```

Application URL:
- http://localhost:8080

## Access Points

- Dashboard: http://localhost:8080
- Products: http://localhost:8080/products
- Customers: http://localhost:8080/customers
- Locations: http://localhost:8080/locations
- Sales: http://localhost:8080/sales
- H2 Console: http://localhost:8080/h2-console
- API Base: http://localhost:8080/api

## Project Layout

```text
src/main/java/com/oltp/
  SalesSystemApplication.java
  config/
  controller/
  dto/
  entity/
  repository/
  service/

src/main/resources/
  application.properties
  templates/
  static/css/modern-ui.css
```

## Data Model Summary

### Core OLTP Entities
- Customer
- Product
- Location
- Sales

### Warehouse Entities (Phase 3)
- stage_sales
- dim_product
- dim_location
- dim_date
- fact_sales
- sales_datamart_daily
- warehouse_pipeline_state

## Sample Data

The application preloads 100 records at startup:
- Sri Lankan customer names and cities
- LKR-based product and sales prices
- Multiple store types and channels
- Realistic transaction history for testing and demos

## REST API Summary

Base URL: http://localhost:8080/api

### Customers
```text
GET    /customers
GET    /customers/{id}
GET    /customers/email/{email}
POST   /customers
PUT    /customers/{id}
DELETE /customers/{id}
```

### Products
```text
GET    /products
GET    /products/{id}
GET    /products/sku/{sku}
GET    /products/category/{category}
GET    /products/reorder
POST   /products
PUT    /products/{id}
PUT    /products/{id}/stock?quantity={n}
DELETE /products/{id}
```

### Locations
```text
GET    /locations
GET    /locations/{id}
GET    /locations/active
POST   /locations
PUT    /locations/{id}
DELETE /locations/{id}
```

### Sales
```text
GET    /sales
GET    /sales/{id}
GET    /sales/date-range?startDate=...&endDate=...
GET    /sales/product-location?productId={id}&locationId={id}&startDate=...&endDate=...
GET    /sales/analytics/product-location?productId={id}&locationId={id}&startDate=...&endDate=...
POST   /sales
PUT    /sales/{id}
DELETE /sales/{id}
```

### Warehouse and Benchmark
```text
GET/POST /benchmark/warehouse/rebuild
GET/POST /benchmark/warehouse/incremental/run
GET      /benchmark/warehouse/status
GET      /benchmark/warehouse/reconcile?startDate=...&endDate=...
GET      /benchmark/datamart/daily?startDate=...&endDate=...
GET      /benchmark/datamart/top-products?limit=10
GET      /benchmark/datamart/top-locations?limit=10
GET      /benchmark/sales-compare?productId={id}&locationId={id}&startDate=...&endDate=...&runs=30
```

## Configuration

Main configuration file:
- src/main/resources/application.properties

Default runtime profile:
- Embedded H2 in-memory database
- Schema auto-create and drop for development
- H2 web console enabled

You can switch to MySQL or PostgreSQL by updating datasource and dialect settings and adding the corresponding JDBC dependency in pom.xml.

## OLTP Design Principles Demonstrated

1. Normalization for transactional consistency
2. Proper indexing for lookup and date filtering
3. Foreign key constraints for data integrity
4. ACID transaction behavior for reliable writes
5. Audit fields for traceability
6. Real-time stock and order state updates

## Troubleshooting

Common issues:
- Port conflict on 8080: change server.port
- Java or Maven not found: verify installation and PATH
- Startup failures: run Maven with full logs
- Empty data: confirm DataLoader execution and H2 console access

Useful command:

```bash
mvn spring-boot:run
```

## Additional Documentation

- FULL_PROJECT_DOCUMENTATION.md
- OLTP_DESIGN.md
- ER_DIAGRAM.md
- PHASES.md

## Project Details

- Repository: https://github.com/visurarodrigo/OLTP-Sales-Management-System-
- Branch: main
- License: Educational and Open Source
- Author: Visura Rodrigo
- Last Updated: April 10, 2026

## License

This project is provided for educational use. You can learn from it, modify it, and extend it for academic or personal projects.
