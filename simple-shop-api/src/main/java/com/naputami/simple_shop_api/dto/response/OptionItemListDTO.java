package com.naputami.simple_shop_api.dto.response;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionItemListDTO {
    private String id;
    private String name;
    private Double price;

}
