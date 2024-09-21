package com.naputami.simple_shop_api.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naputami.simple_shop_api.dto.request.OrderFormDTO;
import com.naputami.simple_shop_api.model.Order;
import com.naputami.simple_shop_api.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/orders")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> addNewOrder(@Valid @ModelAttribute OrderFormDTO request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.toList()));
        }

        return orderService.addNewOrder(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOrder(@PathVariable String id, @Valid @ModelAttribute OrderFormDTO request,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.toList()));
        }

        return orderService.editOrder(request, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        return orderService.deleteOrder(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailOrder(@PathVariable String id) {
        return orderService.getDetailOrder(id);
    }

    @GetMapping
    public ResponseEntity<?> getMethodName(@RequestParam(required = false) String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate,desc") String[] sort) {

          Sort.Direction direction = Sort.Direction.ASC;
        if (sort[1].equals("desc")) {
            direction = Sort.Direction.DESC;
        }

        Sort sortObj = Sort.by(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Specification<Order> spec = (root, query, criteriaBuilder) -> code != null
                ? criteriaBuilder.like(root.get("code"),
                        "%" + code + "%")
                : criteriaBuilder.conjunction();
        
        return orderService.getAllOrders(spec, pageable);

    }

}
