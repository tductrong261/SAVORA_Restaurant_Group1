package vn.edu.fpt.savora.dto.customer.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReservationRequest {

    @NotNull(message = "Please select a dining table")
    private Integer tableId;

    @NotNull(message = "Please select a reservation date")
    @FutureOrPresent(message = "Reservation date cannot be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate reservationDate;

    @NotNull(message = "Please select an arrival time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime reservationTime;

    @NotNull(message = "Please specify your party size")
    @Min(value = 1, message = "Party size must be at least 1 guest")
    @Max(value = 30, message = "Party size must not exceed 30 guests")
    private Integer guestCount;

    @NotBlank(message = "Contact phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must contain 10 to 15 digits")
    private String customerPhone;

    @Size(max = 500, message = "Special request must not exceed 500 characters")
    private String specialRequest;
}
