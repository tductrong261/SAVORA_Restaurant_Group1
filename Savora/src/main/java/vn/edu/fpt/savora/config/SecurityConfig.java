package vn.edu.fpt.savora.config;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import vn.edu.fpt.savora.dto.auth.response.AuthUserResponse;
import vn.edu.fpt.savora.service.auth.AuthService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**", "/staff/**", "/customer/**", "/api/admin/**",
                        "/api/staff/**", "/api/customer/**").permitAll()
                .anyRequest().permitAll())
            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                    AuthUserResponse user = authService.loginWithOAuth2(principal);
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("roleId", user.getRoleId());
                    session.setAttribute("roleName", user.getRoleName());
                    session.setAttribute("fullName", user.getFullName());
                    session.setAttribute("authUser", user);
                    response.sendRedirect(user.getRedirectUrl() != null ? user.getRedirectUrl() : "/home");
                })
                .failureUrl("/login?error=oauth2_failed"))
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"));

        return http.build();
    }
}
