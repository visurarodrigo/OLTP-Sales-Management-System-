package com.oltp.controller;

import com.oltp.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/locations")
@RequiredArgsConstructor
public class WebLocationController {

    private final LocationService locationService;

    @GetMapping
    public String listLocations(Model model) {
        model.addAttribute("locations", locationService.getAllLocations());
        return "locations";
    }
}
