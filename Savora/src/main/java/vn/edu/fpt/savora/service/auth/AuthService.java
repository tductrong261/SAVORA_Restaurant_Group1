package vn.edu.fpt.savora.service.auth;

import org.springframework.security.oauth2.core.user.OAuth2User;
import vn.edu.fpt.savora.dto.auth.request.LoginRequest;
import vn.edu.fpt.savora.dto.auth.request.RegisterRequest;
import vn.edu.fpt.savora.dto.auth.response.AuthUserResponse;

/**
 * Interface xử lý nghiệp vụ xác thực người dùng (Email/Mật khẩu, Đăng ký, Google OAuth2 và Facebook OAuth2)
 */
public interface AuthService {

    AuthUserResponse login(LoginRequest request);

    AuthUserResponse register(RegisterRequest request);

    AuthUserResponse loginWithOAuth2(OAuth2User oauth2User);
}
