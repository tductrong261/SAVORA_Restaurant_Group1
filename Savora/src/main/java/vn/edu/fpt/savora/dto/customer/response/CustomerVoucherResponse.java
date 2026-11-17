package vn.edu.fpt.savora.dto.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerVoucherResponse {
    private Integer voucherId;
    private String voucherCode;
    private String description;
    private Integer discountPercent;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderValue;
    private Integer quantity;
    private LocalDateTime endDate;
    private String status;
}
