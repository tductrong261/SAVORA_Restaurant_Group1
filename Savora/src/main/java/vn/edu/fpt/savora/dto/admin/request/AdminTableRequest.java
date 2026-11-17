package vn.edu.fpt.savora.dto.admin.request;

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
public class AdminTableRequest {

    @NotBlank(message = "Table code is required (e.g. T1-B05)")
    @Size(max = 30, message = "Table code must not exceed 30 characters")
    private String tableCode;

    @NotBlank(message = "Table name is required")
    @Size(max = 100, message = "Table name must not exceed 100 characters")
    private String tableName;

    @NotBlank(message = "Dining zone / area is required")
    @Size(max = 100, message = "Area must not exceed 100 characters")
    private String area;

    @NotNull(message = "Seating capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1 guest")
    @Max(value = 40, message = "Capacity must not exceed 40 guests")
    private Integer capacity;

    @Size(max = 255, message = "Note must not exceed 255 characters")
    private String note;

    private String status;
}
