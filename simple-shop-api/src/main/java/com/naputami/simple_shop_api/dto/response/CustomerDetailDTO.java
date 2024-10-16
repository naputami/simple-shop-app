package com.naputami.simple_shop_api.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDetailDTO {
    private String name;
    private String address;
    private String phoneNumber;
    private String imgUrl;
    private String custCode;
    private LocalDate lastOrderDate;
    private boolean isActive;
}
