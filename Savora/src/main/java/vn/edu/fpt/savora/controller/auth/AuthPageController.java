package vn.edu.fpt.savora.controller.auth;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vn.edu.fpt.savora.dto.auth.request.LoginRequest;
import vn.edu.fpt.savora.dto.auth.request.RegisterRequest;
import vn.edu.fpt.savora.dto.auth.response.AuthUserResponse;
import vn.edu.fpt.savora.exception.AppException;
import vn.edu.fpt.savora.service.auth.AuthService;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginRequest());
        }
        return "public/auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginRequest form,
                        BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors()) {
            return "public/auth/login";
        }
        try {
            AuthUserResponse user = authService.login(form);
            storeUser(session, user);
            return "redirect:" + (user.getRedirectUrl() != null ? user.getRedirectUrl() : "/home");
        } catch (AppException | IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "public/auth/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterRequest());
        }
        return "public/auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterRequest form,
                           BindingResult errors, Model model, HttpSession session) {
        if (form.getPassword() != null && form.getConfirmPassword() != null
                && !form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password.mismatch", "Password confirmation does not match");
        }
        if (errors.hasErrors()) {
            return "public/auth/register";
        }
        try {
            AuthUserResponse user = authService.register(form);
            storeUser(session, user);
            return "redirect:/home?registered=true";
        } catch (AppException | IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "public/auth/register";
        }
    }

    private void storeUser(HttpSession session, AuthUserResponse user) {
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("roleId", user.getRoleId());
        session.setAttribute("roleName", user.getRoleName());
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("authUser", user);
    }
}
