package vn.edu.fpt.savora.dto.admin.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
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
public class AdminVoucherRequest {

    @NotBlank(message = "Voucher code is required")
    @Size(max = 50, message = "Voucher code must not exceed 50 characters")
    private String voucherCode;

    @NotBlank(message = "Voucher description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 1, message = "Discount must be at least 1%")
    @Max(value = 90, message = "Discount must not exceed 90%")
    private Integer discountPercent;

    @NotNull(message = "Maximum discount cap is required")
    @DecimalMin(value = "1000", message = "Max discount must be at least 1,000 VND")
    private BigDecimal maxDiscountAmount;

    @NotNull(message = "Minimum order value is required")
    @DecimalMin(value = "0", message = "Minimum order value cannot be negative")
    private BigDecimal minOrderValue;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
