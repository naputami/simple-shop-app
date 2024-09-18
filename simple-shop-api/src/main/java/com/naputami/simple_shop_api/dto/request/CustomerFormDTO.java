package com.naputami.simple_shop_api.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerFormDTO {
    private MultipartFile imgFile;
    private String name;
    private String address;
    private String phoneNumber;
    private Boolean isActive;
}
