package com.naputami.simple_shop_api.dto.response;

import java.sql.Timestamp;

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
    private String address;
    private String imgUrl;
    private String custCode;
}
