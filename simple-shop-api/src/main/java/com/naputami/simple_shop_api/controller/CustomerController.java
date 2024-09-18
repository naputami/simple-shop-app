package com.naputami.simple_shop_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naputami.simple_shop_api.dto.request.CustomerFormDTO;
import com.naputami.simple_shop_api.dto.response.StandardResponseDTO;
import com.naputami.simple_shop_api.service.CustomerService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<StandardResponseDTO> addNewCustomer(@ModelAttribute CustomerFormDTO request) {
        return customerService.addNewCustomer(request);
    }

    @GetMapping
    public String getMethodName() {
        return "Hello!";
    }

}
