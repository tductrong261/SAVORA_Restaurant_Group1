package vn.edu.fpt.savora.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminTableResponse {
    private Integer tableId;
    private String tableCode;
    private String tableName;
    private String area;
    private Integer capacity;
    private String status;
    private String note;
    private LocalDateTime updatedAt;
}
