package com.naputami.simple_shop_api.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderListItemDTO {
    private String code;
    private String id;
    private LocalDate orderDate;
    private Double totalPrice;
}
