package com.naputami.simple_shop_api.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naputami.simple_shop_api.dto.request.ItemFormDTO;
import com.naputami.simple_shop_api.dto.response.StandardResponseDTO;
import com.naputami.simple_shop_api.model.Item;
import com.naputami.simple_shop_api.service.ItemService;

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
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemControler {
    private final ItemService itemService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<?> addNewItem(@Valid @ModelAttribute ItemFormDTO request, BindingResult result) {
        if (result.hasErrors()) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            StandardResponseDTO res = StandardResponseDTO.builder()
                    .code(status.value())
                    .status(status.getReasonPhrase())
                    .message(messageSource.getMessage("api.error.validation", null, null))
                    .data(result.getAllErrors().stream()
                            .map(error -> error.getDefaultMessage()).collect(Collectors.toList()))
                    .build();

            return ResponseEntity.badRequest().body(res);
        }

        return itemService.addNewItem(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editItem(@PathVariable String id, @Valid @ModelAttribute ItemFormDTO request,
            BindingResult result) {
        if (result.hasErrors()) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            StandardResponseDTO res = StandardResponseDTO.builder()
                    .code(status.value())
                    .status(status.getReasonPhrase())
                    .message(messageSource.getMessage("api.error.validation", null, null))
                    .data(result.getAllErrors().stream()
                            .map(error -> error.getDefaultMessage()).collect(Collectors.toList()))
                    .build();

            return ResponseEntity.badRequest().body(res);
        }

        return itemService.editItem(request, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable String id) {
        return itemService.deleteItem(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailItem(@PathVariable String id) {
        return itemService.getItemDetail(id);
    }

    @GetMapping
    public ResponseEntity<?> getItems(@RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        Sort.Direction direction = Sort.Direction.ASC;
        if (sort[1].equals("desc")) {
            direction = Sort.Direction.DESC;
        }

        Sort sortObj = Sort.by(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Specification<Item> spec = (root, query, criteriaBuilder) -> name != null
                ? criteriaBuilder.like(root.get("name"),
                        "%" + name + "%")
                : criteriaBuilder.conjunction();

        return itemService.getAllItems(spec, pageable);
    }

}
