package com.oltp.controller;

import com.oltp.entity.Customer;
import com.oltp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class WebCustomerController {

    private final CustomerService customerService;

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customers";
    }

    @GetMapping("/new")
    public String showCustomerForm(Model model) {
        Customer customer = new Customer();
        customer.setCustomerStatus("ACTIVE");
        model.addAttribute("customer", customer);
        return "customer-form";
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable Long id, Model model) {
        return customerService.getCustomerById(id)
                .map(customer -> {
                    model.addAttribute("customer", customer);
                    return "customer-form";
                })
                .orElse("redirect:/customers");
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute Customer customer) {
        if (customer.getCustomerStatus() == null || customer.getCustomerStatus().isEmpty()) {
            customer.setCustomerStatus("ACTIVE");
        }
        customerService.saveCustomer(customer);
        return "redirect:/customers";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute Customer customer) {
        // Fetch existing customer to preserve fields not in the form
        Customer existingCustomer = customerService.getCustomerById(id).orElse(null);
        if (existingCustomer == null) {
            return "redirect:/customers";
        }
        
        // Update only the fields from the form
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setAddress(customer.getAddress());
        
        // Save the updated customer
        customerService.saveCustomer(existingCustomer);
        return "redirect:/customers";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
