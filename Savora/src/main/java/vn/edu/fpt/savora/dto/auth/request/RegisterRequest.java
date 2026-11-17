package vn.edu.fpt.savora.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Please enter your first name")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    private String lastName;

    @NotBlank(message = "Please enter your email address")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Please enter a valid email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Please enter your phone number")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must contain 10 to 15 digits")
    private String phone;

    private String gender;

    @NotBlank(message = "Please enter a password")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;
}
