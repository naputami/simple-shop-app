package com.naputami.simple_shop_api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naputami.simple_shop_api.service.OptionService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
;

@RestController
@RequestMapping("/options")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OptionController {
    private final OptionService optionService;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomerOptions() {
        return optionService.getCustomerOptions();
    }

    @GetMapping("/items")
    public ResponseEntity<?> getItemOptions() {
        return optionService.getItemOptions();
    }
    

}
