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

        locations.add(createLocation("STR001", "Colombo Fort Store", "RETAIL",
            "123 Galle Road", "Colombo", "Western", "Sri Lanka", "00100",
            "+94-11-200-0001", "colombofort@store.lk", "Nimal Perera", 500));

        locations.add(createLocation("STR002", "Kandy City Mall", "RETAIL",
            "456 Peradeniya Road", "Kandy", "Central", "Sri Lanka", "20000",
            "+94-81-200-0002", "kandycity@store.lk", "Sajini Fernando", 800));

        locations.add(createLocation("STR003", "Galle Harbor Outlet", "OUTLET",
            "789 Matara Road", "Galle", "Southern", "Sri Lanka", "80000",
            "+94-91-200-0003", "galleharbor@store.lk", "Kavindu Silva", 600));

        locations.add(createLocation("WHS001", "Kelaniya Central Warehouse", "WAREHOUSE",
            "1000 Kandy Road", "Kelaniya", "Western", "Sri Lanka", "11600",
            "+94-11-200-0004", "warehouse@store.lk", "Ruwan Jayasinghe", 10000));

        locations.add(createLocation("STR004", "Negombo Beach Store", "RETAIL",
            "200 Beach Road", "Negombo", "Western", "Sri Lanka", "11500",
            "+94-31-200-0005", "negombo@store.lk", "Ishara Senanayake", 450));

        locations.add(createLocation("ONL001", "Lanka Online Store", "ONLINE",
            "Virtual Commerce Hub", "Colombo", "Western", "Sri Lanka", "00200",
            "+94-11-200-0006", "online@store.lk", "Digital Team", 0));

        String[] cities = {"Jaffna", "Kurunegala", "Matara", "Anuradhapura", "Batticaloa", "Trincomalee", "Ratnapura", "Badulla"};
        String[] states = {"Northern", "North Western", "Southern", "North Central", "Eastern", "Eastern", "Sabaragamuwa", "Uva"};
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
                    (100 + index) + " Main Road",
                    city,
                    state,
                    "Sri Lanka",
                    String.format("%05d", 40000 + index),
                    "+94-70-7" + String.format("%04d", index),
                    "location" + index + "@store.lk",
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

        customers.add(createCustomer("Kasun", "Perera", "kasun.perera@email.lk", "+94-71-000-0101",
            "1990-05-15", "100 Galle Road", "Colombo", "Western", "Sri Lanka", "00300"));

        customers.add(createCustomer("Nadeesha", "Silva", "nadeesha.silva@email.lk", "+94-71-000-0102",
            "1985-08-22", "200 Peradeniya Road", "Kandy", "Central", "Sri Lanka", "20000"));

        customers.add(createCustomer("Tharindu", "Fernando", "tharindu.f@email.lk", "+94-71-000-0103",
            "1992-03-10", "300 Matara Road", "Galle", "Southern", "Sri Lanka", "80000"));

        customers.add(createCustomer("Chathuri", "Jayawardena", "chathuri.j@email.lk", "+94-71-000-0104",
            "1988-11-30", "400 Temple Road", "Jaffna", "Northern", "Sri Lanka", "40000"));

        customers.add(createCustomer("Dilshan", "Wijesinghe", "dilshan.w@email.lk", "+94-71-000-0105",
            "1995-07-18", "500 Lake Road", "Kurunegala", "North Western", "Sri Lanka", "60000"));

        customers.add(createCustomer("Sanduni", "Bandara", "sanduni.b@email.lk", "+94-71-000-0106",
            "1991-01-25", "600 Station Road", "Negombo", "Western", "Sri Lanka", "11500"));

        customers.add(createCustomer("Ravindu", "Gunasekara", "ravindu.g@email.lk", "+94-71-000-0107",
            "1987-09-14", "700 New Town Road", "Anuradhapura", "North Central", "Sri Lanka", "50000"));

        customers.add(createCustomer("Iresha", "Kumari", "iresha.k@email.lk", "+94-71-000-0108",
            "1993-12-05", "800 Market Road", "Batticaloa", "Eastern", "Sri Lanka", "30000"));

        customers.add(createCustomer("Sameera", "Ekanayake", "sameera.e@email.lk", "+94-71-000-0109",
            "1989-04-20", "900 Harbour Road", "Trincomalee", "Eastern", "Sri Lanka", "31000"));

        customers.add(createCustomer("Ayesha", "Nawaz", "ayesha.n@email.lk", "+94-71-000-0110",
            "1994-06-08", "1000 Main Street", "Badulla", "Uva", "Sri Lanka", "90000"));

        String[] cities = {"Colombo", "Kandy", "Galle", "Jaffna", "Kurunegala", "Negombo", "Anuradhapura", "Batticaloa", "Trincomalee", "Badulla"};
        String[] states = {"Western", "Central", "Southern", "Northern", "North Western", "Western", "North Central", "Eastern", "Eastern", "Uva"};
        String[] firstNames = {"Amaya", "Kavindu", "Nethmi", "Sahan", "Ishani", "Thilina", "Dinuka", "Shavindi", "Prabath", "Hasini", "Chamara", "Maleesha"};
        String[] lastNames = {"Perera", "Silva", "Fernando", "Wijesinghe", "Jayawardena", "Gunasekara", "Ekanayake", "Bandara", "Senanayake", "Ranasinghe"};

        while (customers.size() < TARGET_RECORDS) {
            int index = customers.size() + 1;
            int cityIndex = (index - 1) % cities.length;
            LocalDate dob = LocalDate.of(1975 + (index % 25), ((index % 12) + 1), ((index % 28) + 1));
            String firstName = firstNames[(index - 1) % firstNames.length];
            String lastName = lastNames[((index - 1) / firstNames.length) % lastNames.length];
            String emailLocalPart = (firstName + "." + lastName + "." + index).toLowerCase(Locale.ROOT);

            customers.add(createCustomer(
                    firstName,
                    lastName,
                    emailLocalPart + "@email.lk",
                    "+94-72-2" + String.format("%04d", index),
                    dob.toString(),
                    (200 + index) + " Temple Road",
                    cities[cityIndex],
                    states[cityIndex],
                    "Sri Lanka",
                    String.format("%05d", 50000 + index)
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

        products.add(createProduct("TEA001", "Ceylon Tea Gift Pack", "Premium Ceylon black tea assortment", 
            "Groceries", "Tea", "2490.00", "1490.00", 150, 30, "LankaTea"));

        products.add(createProduct("SPC001", "Ceylon Cinnamon Sticks", "Export-grade cinnamon bundle", 
            "Groceries", "Spices", "1290.00", "690.00", 250, 50, "SpiceIsland"));

        products.add(createProduct("APP001", "Batik Saree", "Handcrafted Sri Lankan batik saree", 
            "Clothing", "Traditional Wear", "15990.00", "8990.00", 90, 20, "BatikCeylon"));

        products.add(createProduct("APP002", "Sarong Cotton", "Classic cotton sarong", 
            "Clothing", "Traditional Wear", "3490.00", "1890.00", 180, 40, "IslandWeave"));

        products.add(createProduct("FOOD001", "Kithul Treacle", "Natural palm syrup from Sri Lanka", 
            "Groceries", "Sweeteners", "1890.00", "990.00", 140, 30, "RuhunuFoods"));

        products.add(createProduct("HOME001", "Clay Pot Set", "Traditional earthenware cooking pots", 
            "Home & Kitchen", "Cookware", "4290.00", "2390.00", 100, 20, "CeylonHome"));

        products.add(createProduct("HOME002", "Coconut Shell Bowl Set", "Eco-friendly handcrafted bowls", 
            "Home & Kitchen", "Serveware", "2990.00", "1590.00", 130, 25, "EcoLanka"));

        products.add(createProduct("BOOK001", "History of Ceylon", "Illustrated history book", 
            "Books", "History", "2490.00", "1290.00", 80, 15, "SerendibPress"));

        products.add(createProduct("BOOK002", "Sinhala Folk Tales", "Collection of classic folk stories", 
            "Books", "Culture", "2190.00", "1190.00", 110, 20, "LakbimaBooks"));

        products.add(createProduct("SPORT001", "Cricket Bat", "Seasoned willow cricket bat", 
            "Sports", "Cricket", "8990.00", "4990.00", 95, 20, "LionSport"));

        products.add(createProduct("SPORT002", "Cricket Ball Pack", "Match-grade leather cricket balls", 
            "Sports", "Cricket", "2490.00", "1390.00", 200, 40, "LionSport"));

        products.add(createProduct("BEAUTY001", "Sandalwood Soap Set", "Herbal soap made with sandalwood", 
            "Beauty", "Skincare", "1690.00", "890.00", 160, 35, "AyuCare"));

        String[] categories = {"Groceries", "Home & Kitchen", "Clothing", "Books", "Sports", "Beauty"};
        String[] subCategories = {"Tea", "Cookware", "Traditional Wear", "Culture", "Cricket", "Skincare"};
        String[] brands = {"LankaEssentials", "CeylonHome", "IslandWear", "SerendibPress", "LionSport", "AyuCare"};

        while (products.size() < TARGET_RECORDS) {
            int index = products.size() + 1;
            int bucket = (index - 1) % categories.length;
            double price = 850.0 + random.nextInt(24951) + (random.nextInt(100) / 100.0);
            double cost = price * (0.55 + (random.nextInt(20) / 100.0));

            products.add(createProduct(
                    String.format("GEN%03d", index),
                subCategories[bucket] + " Lanka Product " + index,
                "Auto-generated Sri Lanka product for " + categories[bucket] + " catalog " + index,
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
