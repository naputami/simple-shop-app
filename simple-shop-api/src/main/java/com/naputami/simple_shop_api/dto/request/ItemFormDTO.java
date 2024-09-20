package com.naputami.simple_shop_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.naputami.simple_shop_api.validation.ValidImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemFormDTO {
    @ValidImage
    private MultipartFile imgFile;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must not be lower than 0")
    private Integer stock;

    @NotNull(message = "Price is required")
    @Min(value = 100, message = "Price must not be lower than 100")
    private Double price;

    @NotNull(message = "Last restock date is required")
    private LocalDate lastRestockDate;

    private String desc;
}
