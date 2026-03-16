package com.oltp.config;

import com.oltp.entity.Customer;
import com.oltp.entity.Location;
import com.oltp.entity.Product;
import com.oltp.entity.Sales;
import com.oltp.service.CustomerService;
import com.oltp.service.LocationService;
import com.oltp.service.ProductService;
import com.oltp.service.SalesService;
import com.oltp.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Data Loader - Populates database with synthetic data
 * This runs automatically when the application starts
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private static final int TARGET_RECORDS = 100;

    private final CustomerService customerService;
    private final ProductService productService;
    private final LocationService locationService;
    private final SalesService salesService;
    private final WarehouseService warehouseService;
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        log.info("Starting data loading...");

        // Load Locations first
        List<Location> locations = loadLocations();
        log.info("Loaded {} locations", locations.size());

        // Load Customers
        List<Customer> customers = loadCustomers();
        log.info("Loaded {} customers", customers.size());

        // Load Products
        List<Product> products = loadProducts();
        log.info("Loaded {} products", products.size());

        // Load Sales
        List<Sales> sales = loadSales(customers, products, locations);
        log.info("Loaded {} sales transactions", sales.size());

        warehouseService.rebuildWarehouse();
        log.info("Built dimensional model (fact + dimensions) from OLTP sales");

        log.info("Data loading completed successfully!");
    }

    private List<Location> loadLocations() {
        List<Location> locations = new ArrayList<>();

        locations.add(createLocation("STR001", "Downtown Store", "RETAIL", 
            "123 Main Street", "New York", "NY", "USA", "10001", 
            "212-555-0001", "downtown@store.com", "John Manager", 500));

        locations.add(createLocation("STR002", "Westside Mall", "RETAIL", 
            "456 West Avenue", "Los Angeles", "CA", "USA", "90001", 
            "213-555-0002", "westside@store.com", "Sarah Smith", 800));

        locations.add(createLocation("STR003", "East Bay Outlet", "OUTLET", 
            "789 Bay Road", "San Francisco", "CA", "USA", "94102", 
            "415-555-0003", "eastbay@store.com", "Mike Johnson", 600));

        locations.add(createLocation("WHS001", "Central Warehouse", "WAREHOUSE", 
            "1000 Industrial Blvd", "Chicago", "IL", "USA", "60601", 
            "312-555-0004", "warehouse@store.com", "Robert Brown", 10000));

        locations.add(createLocation("STR004", "Miami Beach Store", "RETAIL", 
            "200 Ocean Drive", "Miami", "FL", "USA", "33139", 
            "305-555-0005", "miami@store.com", "Lisa Garcia", 450));

        locations.add(createLocation("ONL001", "Online Store", "ONLINE", 
            "Virtual Location", "Seattle", "WA", "USA", "98101", 
            "800-555-0006", "online@store.com", "Digital Team", 0));

        String[] cities = {"Dallas", "Houston", "Atlanta", "Portland", "Las Vegas", "Orlando", "Nashville", "Detroit"};
        String[] states = {"TX", "TX", "GA", "OR", "NV", "FL", "TN", "MI"};
        String[] locationTypes = {"RETAIL", "OUTLET", "WAREHOUSE", "ONLINE"};

        while (locations.size() < TARGET_RECORDS) {
            int index = locations.size() + 1;
            int cityIndex = (index - 1) % cities.length;
            String storeCode = String.format("LOC%03d", index);
            String city = cities[cityIndex];
            String state = states[cityIndex];
            String type = locationTypes[(index - 1) % locationTypes.length];

            locations.add(createLocation(
                    storeCode,
                    city + " " + type + " " + index,
                    type,
                    (100 + index) + " Commerce St",
                    city,
                    state,
                    "USA",
                    String.format("%05d", 10000 + index),
                    "555-7" + String.format("%04d", index),
                    "location" + index + "@store.com",
                    "Manager " + index,
                    300 + random.nextInt(9701)
            ));
        }

        for (Location location : locations) {
            locationService.saveLocation(location);
        }

        return locations;
    }

    private Location createLocation(String code, String name, String type, String address,
                                    String city, String state, String country, String postal,
                                    String phone, String email, String manager, int capacity) {
        Location location = new Location();
        location.setStoreCode(code);
        location.setStoreName(name);
        location.setLocationType(type);
        location.setAddress(address);
        location.setCity(city);
        location.setState(state);
        location.setCountry(country);
        location.setPostalCode(postal);
        location.setPhone(phone);
        location.setEmail(email);
        location.setManagerName(manager);
        location.setOpeningTime(LocalTime.of(9, 0));
        location.setClosingTime(LocalTime.of(21, 0));
        location.setStoreCapacity(capacity);
        location.setLocationStatus("ACTIVE");
        return location;
    }

    private List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();

        customers.add(createCustomer("John", "Doe", "john.doe@email.com", "555-0101", 
            "1990-05-15", "100 Oak St", "New York", "NY", "USA", "10001"));

        customers.add(createCustomer("Jane", "Smith", "jane.smith@email.com", "555-0102", 
            "1985-08-22", "200 Pine Ave", "Los Angeles", "CA", "USA", "90001"));

        customers.add(createCustomer("Michael", "Johnson", "michael.j@email.com", "555-0103", 
            "1992-03-10", "300 Maple Dr", "Chicago", "IL", "USA", "60601"));

        customers.add(createCustomer("Emily", "Williams", "emily.w@email.com", "555-0104", 
            "1988-11-30", "400 Elm Blvd", "San Francisco", "CA", "USA", "94102"));

        customers.add(createCustomer("David", "Brown", "david.brown@email.com", "555-0105", 
            "1995-07-18", "500 Cedar Ln", "Miami", "FL", "USA", "33139"));

        customers.add(createCustomer("Sarah", "Davis", "sarah.davis@email.com", "555-0106", 
            "1991-01-25", "600 Birch Rd", "Seattle", "WA", "USA", "98101"));

        customers.add(createCustomer("Robert", "Miller", "robert.m@email.com", "555-0107", 
            "1987-09-14", "700 Spruce St", "Boston", "MA", "USA", "02101"));

        customers.add(createCustomer("Jennifer", "Wilson", "jennifer.w@email.com", "555-0108", 
            "1993-12-05", "800 Walnut Ave", "Austin", "TX", "USA", "73301"));

        customers.add(createCustomer("James", "Moore", "james.moore@email.com", "555-0109", 
            "1989-04-20", "900 Ash Dr", "Denver", "CO", "USA", "80201"));

        customers.add(createCustomer("Maria", "Garcia", "maria.garcia@email.com", "555-0110", 
            "1994-06-08", "1000 Poplar Blvd", "Phoenix", "AZ", "USA", "85001"));

        String[] cities = {"New York", "Los Angeles", "Chicago", "San Francisco", "Miami", "Seattle", "Boston", "Austin", "Denver", "Phoenix"};
        String[] states = {"NY", "CA", "IL", "CA", "FL", "WA", "MA", "TX", "CO", "AZ"};

        while (customers.size() < TARGET_RECORDS) {
            int index = customers.size() + 1;
            int cityIndex = (index - 1) % cities.length;
            LocalDate dob = LocalDate.of(1975 + (index % 25), ((index % 12) + 1), ((index % 28) + 1));

            customers.add(createCustomer(
                    "Customer" + index,
                    "User" + index,
                    "customer" + index + "@email.com",
                    "555-2" + String.format("%04d", index),
                    dob.toString(),
                    (200 + index) + " Market Street",
                    cities[cityIndex],
                    states[cityIndex],
                    "USA",
                    String.format("%05d", 20000 + index)
            ));
        }

        for (Customer customer : customers) {
            customerService.saveCustomer(customer);
        }

        return customers;
    }

    private Customer createCustomer(String firstName, String lastName, String email, String phone,
                                    String dob, String address, String city, String state,
                                    String country, String postal) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setDateOfBirth(LocalDate.parse(dob));
        customer.setAddress(address);
        customer.setCity(city);
        customer.setState(state);
        customer.setCountry(country);
        customer.setPostalCode(postal);
        customer.setCustomerStatus("ACTIVE");
        return customer;
    }

    private List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();

        products.add(createProduct("ELEC001", "Laptop Pro 15", "High-performance laptop", 
            "Electronics", "Computers", "1299.99", "899.99", 50, 10, "TechBrand"));

        products.add(createProduct("ELEC002", "Smartphone X", "Latest smartphone", 
            "Electronics", "Mobile", "899.99", "599.99", 100, 20, "PhoneCorp"));

        products.add(createProduct("ELEC003", "Wireless Headphones", "Noise-cancelling headphones", 
            "Electronics", "Audio", "249.99", "149.99", 150, 30, "AudioMax"));

        products.add(createProduct("HOME001", "Coffee Maker Deluxe", "Programmable coffee maker", 
            "Home & Kitchen", "Appliances", "129.99", "69.99", 80, 15, "BrewMaster"));

        products.add(createProduct("HOME002", "Blender Pro", "High-speed blender", 
            "Home & Kitchen", "Appliances", "89.99", "49.99", 60, 15, "KitchenPro"));

        products.add(createProduct("CLOTH001", "Running Shoes", "Comfortable running shoes", 
            "Clothing", "Footwear", "119.99", "59.99", 200, 40, "SportFit"));

        products.add(createProduct("CLOTH002", "Winter Jacket", "Warm winter jacket", 
            "Clothing", "Outerwear", "199.99", "99.99", 75, 15, "WarmWear"));

        products.add(createProduct("BOOK001", "Python Programming", "Complete guide to Python", 
            "Books", "Technology", "49.99", "24.99", 120, 25, "TechPublishers"));

        products.add(createProduct("BOOK002", "Science Fiction Novel", "Bestselling sci-fi book", 
            "Books", "Fiction", "24.99", "12.99", 180, 35, "NovelHouse"));

        products.add(createProduct("SPORT001", "Yoga Mat", "Premium yoga mat", 
            "Sports", "Fitness", "39.99", "19.99", 150, 30, "FitLife"));

        products.add(createProduct("SPORT002", "Dumbbell Set", "Adjustable dumbbell set", 
            "Sports", "Fitness", "149.99", "89.99", 50, 10, "StrengthCo"));

        products.add(createProduct("BEAUTY001", "Skincare Set", "Complete skincare routine", 
            "Beauty", "Skincare", "79.99", "39.99", 90, 20, "GlowBeauty"));

        String[] categories = {"Electronics", "Home & Kitchen", "Clothing", "Books", "Sports", "Beauty"};
        String[] subCategories = {"Computers", "Appliances", "Footwear", "Fiction", "Fitness", "Skincare"};
        String[] brands = {"NovaTech", "UrbanHome", "PeakFit", "ReadHouse", "ActiveCore", "PureGlow"};

        while (products.size() < TARGET_RECORDS) {
            int index = products.size() + 1;
            int bucket = (index - 1) % categories.length;
            double price = 20.0 + random.nextInt(480) + (random.nextInt(100) / 100.0);
            double cost = price * (0.55 + (random.nextInt(20) / 100.0));

            products.add(createProduct(
                    String.format("GEN%03d", index),
                    categories[bucket] + " Item " + index,
                    "Auto-generated sample product " + index,
                    categories[bucket],
                    subCategories[bucket],
                    String.format(Locale.US, "%.2f", price),
                    String.format(Locale.US, "%.2f", cost),
                    40 + random.nextInt(220),
                    10 + random.nextInt(25),
                    brands[bucket]
            ));
        }

        for (Product product : products) {
            productService.saveProduct(product);
        }

        return products;
    }

    private Product createProduct(String sku, String name, String description, String category,
                                 String subCategory, String price, String cost, int stock,
                                 int reorder, String brand) {
        Product product = new Product();
        product.setSku(sku);
        product.setProductName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setPrice(new BigDecimal(price));
        product.setCostPrice(new BigDecimal(cost));
        product.setStockQuantity(stock);
        product.setReorderLevel(reorder);
        product.setProductStatus("AVAILABLE");
        product.setBrand(brand);
        product.setWeight(new BigDecimal("1.5"));
        return product;
    }

    private List<Sales> loadSales(List<Customer> customers, List<Product> products, List<Location> locations) {
        List<Sales> sales = new ArrayList<>();
        String[] paymentMethods = {"CASH", "CREDIT_CARD", "DEBIT_CARD", "DIGITAL_WALLET"};
        String[] orderStatuses = {"COMPLETED", "PROCESSING", "COMPLETED", "COMPLETED"}; // More completed orders

        // Generate 100 sales transactions
        for (int i = 0; i < TARGET_RECORDS; i++) {
            Customer customer = customers.get(random.nextInt(customers.size()));
            Product product = products.get(random.nextInt(products.size()));
            Location location = locations.get(random.nextInt(locations.size()));

            Sales sale = new Sales();
            sale.setOrderNumber("ORD" + String.format("%06d", 1000 + i));
            sale.setCustomer(customer);
            sale.setProduct(product);
            sale.setLocation(location);
            
            int quantity = random.nextInt(5) + 1; // 1-5 items
            sale.setQuantity(quantity);
            sale.setUnitPrice(product.getPrice());
            
            // Random discount (0-20%)
            BigDecimal discount = product.getPrice()
                .multiply(new BigDecimal(quantity))
                .multiply(new BigDecimal(random.nextInt(21) / 100.0));
            sale.setDiscountAmount(discount);
            
            sale.setPaymentMethod(paymentMethods[random.nextInt(paymentMethods.length)]);
            sale.setPaymentStatus("PAID");
            sale.setOrderStatus(orderStatuses[random.nextInt(orderStatuses.length)]);
            
            // Random sale date within last 30 days
            LocalDateTime saleDate = LocalDateTime.now().minusDays(random.nextInt(30));
            sale.setSaleDate(saleDate);
            
            if (sale.getOrderStatus().equals("COMPLETED")) {
                sale.setDeliveryDate(saleDate.plusDays(random.nextInt(7) + 1));
            }

            try {
                salesService.createSale(sale);
                sales.add(sale);
            } catch (Exception e) {
                log.warn("Could not create sale {}: {}", sale.getOrderNumber(), e.getMessage());
            }
        }

        return sales;
    }
}
