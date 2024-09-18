package com.naputami.simple_shop_api.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeGeneratorService {
    
    private static final String CUST_PREFIX = "CUST-";
    private static final int CODE_LENGTH= 5;
    private final Random random = new Random();

    public String generateCustomerCode() {
        int randomNumber = random.nextInt(100000); // Generates a number between 0 and 99999
        String formattedNumber = String.format("%0" + CODE_LENGTH + "d", randomNumber);
        return CUST_PREFIX + formattedNumber;
    }
}
