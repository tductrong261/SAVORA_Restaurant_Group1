package vn.edu.fpt.savora.dto.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrderItemResponse {
    private Integer orderItemId;
    private Integer dishId;
    private String dishName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private String note;
    private String itemStatus;
}
