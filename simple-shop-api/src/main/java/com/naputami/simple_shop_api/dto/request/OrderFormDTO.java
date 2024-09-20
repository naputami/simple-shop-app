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


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFormDTO {
    @NotBlank(message = "Customer id must not be empty")
    private String customerId;
    @NotBlank(message = "Item id must not be empty")
    private String itemId;
    @NotNull(message = "Order date must not be empty")
    private LocalDate orderDate;
    @NotNull(message = "qty must not be empty")
    @Min(value = 1, message = "qty must not be lower than 1")
    private Integer qty;
}
