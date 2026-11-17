package vn.edu.fpt.savora.dto.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerReviewResponse {
    private Integer reviewId;
    private String dishName;
    private String orderCode;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
