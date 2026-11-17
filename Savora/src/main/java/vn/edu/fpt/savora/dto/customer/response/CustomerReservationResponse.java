package vn.edu.fpt.savora.dto.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerReservationResponse {
    private Integer reservationId;
    private String reservationCode;
    private Integer tableId;
    private String tableCode;
    private String tableName;
    private String area;
    private Integer tableCapacity;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private LocalTime endTime;
    private Integer durationMinutes;
    private Integer guestCount;
    private String customerName;
    private String customerPhone;
    private String specialRequest;
    private String status;
    private LocalDateTime createdAt;
}
