package vn.edu.fpt.savora.dto.customer.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotNull(message = "Please select a signature dish")
    private Integer dishId;

    @NotNull(message = "Please enter a quantity")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 20, message = "Maximum 20 portions per order item")
    private Integer quantity;

    private String orderType; // PRE_ORDER or TAKEAWAY

    private Integer reservationId;

    @Size(max = 50, message = "Voucher code is too long")
    private String voucherCode;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;
}
