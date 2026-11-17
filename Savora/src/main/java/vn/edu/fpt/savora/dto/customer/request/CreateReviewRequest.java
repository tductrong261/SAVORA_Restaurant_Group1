package vn.edu.fpt.savora.dto.customer.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class CreateReviewRequest {

    private Integer dishId;

    private Integer orderId;

    @NotNull(message = "Please select a rating from 1 to 5")
    @Min(value = 1, message = "Minimum rating is 1 star")
    @Max(value = 5, message = "Maximum rating is 5 stars")
    private Integer rating;

    @NotBlank(message = "Please share your dining feedback")
    @Size(max = 1000, message = "Review comment must not exceed 1000 characters")
    private String comment;
}
