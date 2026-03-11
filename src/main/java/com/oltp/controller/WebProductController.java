package com.oltp.controller;

import com.oltp.entity.Product;
import com.oltp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class WebProductController {

    private final ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/new")
    public String showProductForm(Model model) {
        Product product = new Product();
        product.setProductStatus("AVAILABLE");
        product.setStockQuantity(0);
        product.setPrice(BigDecimal.ZERO);
        model.addAttribute("product", product);
        return "product-form";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-details";
                })
                .orElse("redirect:/products");
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-form";
                })
                .orElse("redirect:/products");
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        // Auto-generate SKU if not provided
        if (product.getSku() == null || product.getSku().isEmpty()) {
            product.setSku(generateSKU(product.getProductName()));
        }
        
        // Set default product status
        if (product.getProductStatus() == null || product.getProductStatus().isEmpty()) {
            product.setProductStatus("AVAILABLE");
        }
        
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        // Fetch existing product to preserve fields not in the form
        Product existingProduct = productService.getProductById(id).orElse(null);
        if (existingProduct == null) {
            return "redirect:/products";
        }
        
        // Update only the fields from the form
        existingProduct.setProductName(product.getProductName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setDescription(product.getDescription());
        
        // Update SKU if provided
        if (product.getSku() != null && !product.getSku().isEmpty()) {
            existingProduct.setSku(product.getSku());
        }
        
        // Save the updated product
        productService.saveProduct(existingProduct);
        return "redirect:/products";
    }
    
    private String generateSKU(String productName) {
        // Generate SKU: First 3 letters of product name + timestamp
        String prefix = productName.replaceAll("[^A-Za-z]", "").toUpperCase();
        if (prefix.length() > 3) {
            prefix = prefix.substring(0, 3);
        } else if (prefix.isEmpty()) {
            prefix = "PRD";
        }
        return prefix + "-" + System.currentTimeMillis();
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
