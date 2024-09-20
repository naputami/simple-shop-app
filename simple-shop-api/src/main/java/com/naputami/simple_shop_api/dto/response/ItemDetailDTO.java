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
public class ItemDetailDTO {
    private String name;
    private Double price;
    private Integer stock;
    private String imgUrl;
    private LocalDate lastRestockDate;
    private Boolean isAvailable;
    private String itemCode;
    private String desc;
}
