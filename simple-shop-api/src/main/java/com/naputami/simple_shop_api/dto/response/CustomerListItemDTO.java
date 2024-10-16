package com.naputami.simple_shop_api.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerListItemDTO {
    private String id;
    private String name;
    private String imgUrl;
    private String custCode;
    private boolean isActive;
    private LocalDate lastOrderDate;
}
