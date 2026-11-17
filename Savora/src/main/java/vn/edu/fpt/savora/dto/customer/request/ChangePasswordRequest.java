package vn.edu.fpt.savora.dto.customer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {

    @NotBlank(message = "Please enter your current password")
    private String currentPassword;

    @NotBlank(message = "Please enter a new password")
    @Size(min = 6, max = 100, message = "New password must be at least 6 characters long")
    private String newPassword;

    @NotBlank(message = "Please confirm your new password")
    private String confirmPassword;
}
