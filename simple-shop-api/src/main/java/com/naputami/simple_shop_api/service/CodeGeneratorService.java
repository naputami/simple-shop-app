package com.naputami.simple_shop_api.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeGeneratorService {
    
    private static final String CUST_PREFIX = "CUST-";
    private static final String ORD_PREFIX = "ORD-";
    private static final String ITM_PREFIX = "ITM-";
    private static final int CODE_LENGTH= 5;
    private static final Random random = new Random();

    private static String formatCode(){
        int randomNumber = random.nextInt(100000); // Generates a number between 0 and 99999
        String formattedNumber = String.format("%0" + CODE_LENGTH + "d", randomNumber);

        return formattedNumber;
    }

    public String generateCustomerCode() {
        return CUST_PREFIX + formatCode();
    }

    public String generateItemCode(){
        return ITM_PREFIX + formatCode();
    }

    public String generateOrderCode(){
        return ORD_PREFIX + formatCode();
    }
}
