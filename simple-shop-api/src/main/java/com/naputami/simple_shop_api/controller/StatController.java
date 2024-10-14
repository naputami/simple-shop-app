package com.naputami.simple_shop_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.naputami.simple_shop_api.service.StatService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatController {
    private final StatService statService;

    @GetMapping("/stat")
    public ResponseEntity<?> getStat() {
        return statService.getStatInfo();
    }
    
}
