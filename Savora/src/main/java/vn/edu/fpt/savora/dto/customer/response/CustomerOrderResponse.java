package vn.edu.fpt.savora.dto.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrderResponse {
    private Integer orderId;
    private String orderCode;
    private String orderType;
    private String tableCode;
    private String reservationCode;
    private String voucherCode;
    private BigDecimal subtotalAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String status;
    private String note;
    private LocalDateTime createdAt;
    private List<CustomerOrderItemResponse> items;
}
