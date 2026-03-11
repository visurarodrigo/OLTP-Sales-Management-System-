package com.oltp.controller;

import com.oltp.service.CustomerService;
import com.oltp.service.LocationService;
import com.oltp.service.ProductService;
import com.oltp.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CustomerService customerService;
    private final SalesService salesService;
    private final LocationService locationService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalProducts", productService.getAllProducts().size());
        model.addAttribute("totalCustomers", customerService.getAllCustomers().size());
        model.addAttribute("totalSales", salesService.getAllSales().size());
        model.addAttribute("totalLocations", locationService.getAllLocations().size());
        return "index";
    }
}
