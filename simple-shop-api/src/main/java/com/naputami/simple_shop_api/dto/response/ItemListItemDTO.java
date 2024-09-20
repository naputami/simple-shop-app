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
public class ItemListItemDTO {
    private String id;
    private String name;
    private String code;
    private Integer stock;
    private Double price;
    private LocalDate lastRestockDate;
    private Boolean isAvailable;
}
