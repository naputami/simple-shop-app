package com.naputami.simple_shop_api.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
    private String code;
    private String customerName;
    private String itemName;
    private Integer qty;
    private Double totalPrice;
    private LocalDate orderDate;
}
