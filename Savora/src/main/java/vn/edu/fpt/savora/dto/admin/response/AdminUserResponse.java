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
public class AdminUserResponse {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private String gender;
    private String roleName;
    private String status;
    private LocalDateTime createdAt;
}
