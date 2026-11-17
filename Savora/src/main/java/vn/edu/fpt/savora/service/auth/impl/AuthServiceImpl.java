package vn.edu.fpt.savora.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.savora.dto.auth.request.LoginRequest;
import vn.edu.fpt.savora.dto.auth.request.RegisterRequest;
import vn.edu.fpt.savora.dto.auth.response.AuthUserResponse;
import vn.edu.fpt.savora.entity.Role;
import vn.edu.fpt.savora.entity.User;
import vn.edu.fpt.savora.enums.Gender;
import vn.edu.fpt.savora.enums.UserStatus;
import vn.edu.fpt.savora.exception.AppException;
import vn.edu.fpt.savora.exception.ErrorCode;
import vn.edu.fpt.savora.mapper.auth.AuthUserMapper;
import vn.edu.fpt.savora.repository.RoleRepository;
import vn.edu.fpt.savora.repository.UserRepository;
import vn.edu.fpt.savora.security.util.PasswordUtils;
import vn.edu.fpt.savora.service.auth.AuthService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Triển khai nghiệp vụ Đăng nhập bằng Email/Mật khẩu, Đăng ký, Google OAuth2 và Facebook OAuth2
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthUserMapper authUserMapper;

    @Override
    public AuthUserResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (!PasswordUtils.checkPassword(request.getPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (user.getStatus() == UserStatus.LOCKED) {
            throw new AppException(ErrorCode.ACCOUNT_LOCKED);
        }

        if (user.getStatus() == UserStatus.PENDING) {
            throw new AppException(ErrorCode.ACCOUNT_PENDING);
        }

        return buildAuthUserResponse(user);
    }

    /**
     * Register a new Customer account in MySQL
     */
    @Override
    @Transactional
    public AuthUserResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role customerRole = roleRepository.findByRoleName("CUSTOMER")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().roleName("CUSTOMER").description("Customer").build()
                ));

        User newUser = authUserMapper.toEntity(request);
        newUser.setPasswordHash(PasswordUtils.hashPassword(request.getPassword()));
        newUser.setRoles(Set.of(customerRole));

        User savedUser = userRepository.save(newUser);
        return buildAuthUserResponse(savedUser);
    }

    /**
     * Xử lý đăng nhập OAuth2 dùng chung cho cả Google và Facebook
     */
    @Override
    @Transactional
    public AuthUserResponse loginWithOAuth2(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String socialId = oauth2User.getAttribute("id");
        if (socialId == null) {
            socialId = oauth2User.getAttribute("sub");
        }

        if (email == null || email.isBlank()) {
            email = "fb_" + (socialId != null ? socialId : UUID.randomUUID().toString().substring(0, 8)) + "@facebook.savora.vn";
        }
        String normalizedEmail = email.trim().toLowerCase();

        String givenName = firstNonBlank(oauth2User.getAttribute("given_name"), oauth2User.getAttribute("first_name"));
        String familyName = firstNonBlank(oauth2User.getAttribute("family_name"), oauth2User.getAttribute("last_name"));
        String fullNameAttr = oauth2User.getAttribute("name");

        String picture = extractPictureUrl(oauth2User.getAttribute("picture"));

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail).orElse(null);

        if (user == null) {
            Role customerRole = roleRepository.findByRoleName("CUSTOMER")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder().roleName("CUSTOMER").description("Customer").build()
                    ));

            String firstName = (familyName != null && !familyName.isBlank()) ? familyName : "Customer";
            String lastName = (givenName != null && !givenName.isBlank())
                    ? givenName
                    : (fullNameAttr != null ? fullNameAttr : "Savora");

            user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(normalizedEmail)
                    .passwordHash(PasswordUtils.hashPassword(UUID.randomUUID().toString()))
                    .avatarUrl(picture != null ? picture : "https://res.cloudinary.com/dej5mxdrt/image/upload/v1780061324/OIP_dbbjuo.jpg")
                    .gender(Gender.OTHER)
                    .status(UserStatus.ACTIVE)
                    .roles(Set.of(customerRole))
                    .build();

            user = userRepository.save(user);
        } else if (picture != null && !picture.isBlank()) {
            user.setAvatarUrl(picture);
            user = userRepository.save(user);
        }

        return buildAuthUserResponse(user);
    }

    private String extractPictureUrl(Object pictureAttr) {
        if (pictureAttr instanceof String str && !str.isBlank()) {
            return str;
        }
        if (pictureAttr instanceof Map<?, ?> map) {
            Object dataObj = map.get("data");
            if (dataObj instanceof Map<?, ?> dataMap) {
                Object urlObj = dataMap.get("url");
                if (urlObj instanceof String urlStr && !urlStr.isBlank()) {
                    return urlStr;
                }
            }
        }
        return null;
    }

    private String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return null;
    }

    private AuthUserResponse buildAuthUserResponse(User user) {
        Role primaryRole = user.getRoles().stream().findFirst()
                .orElseGet(() -> Role.builder().roleId(2).roleName("CUSTOMER").build());

        String redirectUrl = resolveRedirectUrl(primaryRole.getRoleId());
        return authUserMapper.toAuthUserResponse(user, primaryRole, redirectUrl);
    }

    private String resolveRedirectUrl(Integer roleId) {
        if (roleId == null) {
            return "/home";
        }
        return switch (roleId) {
            case 1 -> "/admin/dashboard";
            case 3, 4 -> "/staff/workspace";
            default -> "/home";
        };
    }
}
