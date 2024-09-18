package com.naputami.simple_shop_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StandardResponseDTO {
    private String status;
    private int code;
    private String message;
    private Object data;
}
