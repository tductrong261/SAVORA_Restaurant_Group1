package vn.edu.fpt.savora.dto.admin.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDishRequest {

    @NotNull(message = "Please select a culinary category")
    private Integer categoryId;

    @NotBlank(message = "Dish name is required")
    @Size(max = 200, message = "Dish name must not exceed 200 characters")
    private String dishName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Standard price is required")
    @DecimalMin(value = "1000", message = "Price must be at least 1,000 VND")
    private BigDecimal price;

    private BigDecimal discountPrice;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String thumbnailUrl;

    @Min(value = 1, message = "Preparation time must be at least 1 minute")
    private Integer preparationTimeMinutes;

    private Boolean isSignature;

    private String status;
}
