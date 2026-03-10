package com.oltp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<html><head><title>OLTP Sales System</title>" +
                "<style>body{font-family:Arial,sans-serif;margin:40px;background:#f5f5f5;}" +
                "h1{color:#2c3e50;}h2{color:#34495e;margin-top:30px;}" +
                "ul{list-style:none;padding:0;}" +
                "li{margin:10px 0;}" +
                "a{color:#3498db;text-decoration:none;padding:8px 15px;background:#fff;border-radius:4px;display:inline-block;}" +
                "a:hover{background:#3498db;color:#fff;}" +
                ".container{max-width:800px;margin:0 auto;background:#fff;padding:30px;border-radius:8px;box-shadow:0 2px 4px rgba(0,0,0,0.1);}" +
                "</style></head><body><div class='container'>" +
                "<h1>🏪 OLTP Sales System - Welcome</h1>" +
                "<p>Your Spring Boot application is running successfully!</p>" +
                
                "<h2>📊 H2 Database Console</h2>" +
                "<ul><li><a href='/h2-console' target='_blank'>Open H2 Console</a></li></ul>" +
                "<p><small>JDBC URL: <code>jdbc:h2:mem:oltp_sales_db</code> | Username: <code>sa</code> | Password: (blank)</small></p>" +
                
                "<h2>🔌 API Endpoints</h2>" +
                
                "<h3>👥 Customers</h3>" +
                "<ul>" +
                "<li><a href='/api/customers' target='_blank'>GET /api/customers</a> - Get all customers</li>" +
                "<li>POST /api/customers - Create new customer</li>" +
                "<li>GET /api/customers/{id} - Get customer by ID</li>" +
                "<li>PUT /api/customers/{id} - Update customer</li>" +
                "<li>DELETE /api/customers/{id} - Delete customer</li>" +
                "</ul>" +
                
                "<h3>📦 Products</h3>" +
                "<ul>" +
                "<li><a href='/api/products' target='_blank'>GET /api/products</a> - Get all products</li>" +
                "<li>POST /api/products - Create new product</li>" +
                "<li>GET /api/products/{id} - Get product by ID</li>" +
                "<li>PUT /api/products/{id} - Update product</li>" +
                "<li>DELETE /api/products/{id} - Delete product</li>" +
                "<li>PUT /api/products/{id}/stock?quantity={qty} - Update stock</li>" +
                "</ul>" +
                
                "<h3>📍 Locations</h3>" +
                "<ul>" +
                "<li><a href='/api/locations' target='_blank'>GET /api/locations</a> - Get all locations</li>" +
                "<li>POST /api/locations - Create new location</li>" +
                "<li>GET /api/locations/{id} - Get location by ID</li>" +
                "<li>PUT /api/locations/{id} - Update location</li>" +
                "<li>DELETE /api/locations/{id} - Delete location</li>" +
                "</ul>" +
                
                "<h3>💰 Sales</h3>" +
                "<ul>" +
                "<li><a href='/api/sales' target='_blank'>GET /api/sales</a> - Get all sales</li>" +
                "<li>POST /api/sales - Create new sale</li>" +
                "<li>GET /api/sales/{id} - Get sale by ID</li>" +
                "<li>PUT /api/sales/{id} - Update sale</li>" +
                "<li>DELETE /api/sales/{id} - Delete sale</li>" +
                "</ul>" +
                
                "<h2>💡 Quick Tips</h2>" +
                "<ul>" +
                "<li>Use tools like Postman or curl to test POST/PUT/DELETE endpoints</li>" +
                "<li>Check the H2 console to view your database tables and data</li>" +
                "<li>Sample data is automatically loaded on startup via DataLoader</li>" +
                "</ul>" +
                
                "</div></body></html>";
    }
}
