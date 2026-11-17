package vn.edu.fpt.savora.dto.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfileResponse {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String status;
    private String roleName;
    private LocalDateTime createdAt;
}
