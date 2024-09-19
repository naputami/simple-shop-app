package com.naputami.simple_shop_api.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.naputami.simple_shop_api.validation.ValidImage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerFormDTO {
    @ValidImage
    private MultipartFile imgFile;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(min = 3, max = 100, message = "Address must be between 3 and 100 characters")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Size(min = 3, max = 20, message = "Phone number must be between 3 and 50 characters")
    private String phoneNumber;
    private Boolean isActive;
}
