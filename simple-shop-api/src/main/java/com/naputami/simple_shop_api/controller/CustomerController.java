package com.naputami.simple_shop_api.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naputami.simple_shop_api.dto.request.CustomerFormDTO;
import com.naputami.simple_shop_api.model.Customer;
import com.naputami.simple_shop_api.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> addNewCustomer(@Valid @ModelAttribute CustomerFormDTO request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.toList()));
        }
        return customerService.addNewCustomer(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editCustomer(@PathVariable String id, @Valid @ModelAttribute CustomerFormDTO request,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.toList()));
        }

        return customerService.editCustomer(request, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerDetail(@PathVariable String id) {
        return customerService.getCustomerDetail(id);
    }

    @GetMapping
    public ResponseEntity<?> getCustomers(@RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sort[1].equals("desc")) {
            direction = Sort.Direction.DESC;
        }

        Sort sortObj = Sort.by(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Specification<Customer> spec = (root, query, criteriaBuilder) -> name != null ? criteriaBuilder.like(root.get("name"),
                "%" + name + "%") : criteriaBuilder.conjunction();

        return customerService.getAllCustomers(spec, pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        return customerService.deleteCustomer(id);
    }

}
