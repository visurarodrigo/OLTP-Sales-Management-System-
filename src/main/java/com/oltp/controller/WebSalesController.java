package com.oltp.controller;

import com.oltp.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/sales")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebSalesController {

    private final SalesService salesService;

    @GetMapping
    public String listSales(Model model) {
        model.addAttribute("sales", salesService.getAllSales());
        
        // Calculate total revenue
        BigDecimal totalRevenue = salesService.getAllSales().stream()
                .map(sale -> sale.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        model.addAttribute("totalRevenue", totalRevenue);
        return "sales";
    }
}
