package vn.edu.fpt.savora.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDishResponse {
    private Integer dishId;
    private Integer categoryId;
    private String categoryName;
    private String dishName;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String thumbnailUrl;
    private Boolean isSignature;
    private Integer preparationTimeMinutes;
    private String status;
}
