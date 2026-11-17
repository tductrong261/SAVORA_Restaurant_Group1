package vn.edu.fpt.savora.controller.customer;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.savora.common.ApiResponse;
import vn.edu.fpt.savora.dto.auth.response.AuthUserResponse;
import vn.edu.fpt.savora.dto.customer.request.ChangePasswordRequest;
import vn.edu.fpt.savora.dto.customer.request.CreateOrderRequest;
import vn.edu.fpt.savora.dto.customer.request.CreateReservationRequest;
import vn.edu.fpt.savora.dto.customer.request.CreateReviewRequest;
import vn.edu.fpt.savora.dto.customer.request.UpdateProfileRequest;
import vn.edu.fpt.savora.dto.customer.response.CustomerOrderResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerProfileResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerReservationResponse;
import vn.edu.fpt.savora.exception.AppException;
import vn.edu.fpt.savora.service.customer.CustomerPortalService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerPortalController {

    private final CustomerPortalService customerPortalService;

    // =====================================================================
    // 1. PROFILE, PASSWORD & VOUCHER WALLET (/customer/profile)
    // =====================================================================

    @GetMapping({"/customer", "/customer/profile"})
    public String profilePage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        CustomerProfileResponse profile = loadProfileSafe(userId, session);
        model.addAttribute("profile", profile);
        model.addAttribute("vouchers", customerPortalService.getActiveVouchers());

        if (!model.containsAttribute("profileForm") && profile != null) {
            model.addAttribute("profileForm", UpdateProfileRequest.builder()
                    .firstName(profile.getFirstName())
                    .lastName(profile.getLastName())
                    .phone(profile.getPhone())
                    .gender(profile.getGender())
                    .dateOfBirth(profile.getDateOfBirth())
                    .address(profile.getAddress())
                    .avatarUrl(profile.getAvatarUrl())
                    .build());
        } else if (!model.containsAttribute("profileForm")) {
            model.addAttribute("profileForm", new UpdateProfileRequest());
        }

        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new ChangePasswordRequest());
        }

        return "customer/profile";
    }

    @PostMapping("/customer/profile/update")
    public String updateProfile(@Valid @ModelAttribute("profileForm") UpdateProfileRequest form,
                                BindingResult errors,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (errors.hasErrors()) {
            model.addAttribute("profile", loadProfileSafe(userId, session));
            model.addAttribute("vouchers", customerPortalService.getActiveVouchers());
            model.addAttribute("passwordForm", new ChangePasswordRequest());
            model.addAttribute("errorMessage", "Please review the highlighted profile fields.");
            return "customer/profile";
        }

        try {
            CustomerProfileResponse updated = customerPortalService.updateProfile(userId, form);
            session.setAttribute("fullName", updated.getFullName());
            AuthUserResponse sessionUser = (AuthUserResponse) session.getAttribute("authUser");
            if (sessionUser != null) {
                sessionUser.setFullName(updated.getFullName());
                sessionUser.setPhone(updated.getPhone());
                sessionUser.setAvatarUrl(updated.getAvatarUrl());
                session.setAttribute("authUser", sessionUser);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Your personal profile has been updated successfully.");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/profile";
    }

    @PostMapping("/customer/profile/password")
    public String changePassword(@Valid @ModelAttribute("passwordForm") ChangePasswordRequest form,
                                 BindingResult errors,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (form.getNewPassword() != null && form.getConfirmPassword() != null
                && !form.getNewPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password.mismatch", "Password confirmation does not match");
        }

        if (errors.hasErrors()) {
            CustomerProfileResponse profile = loadProfileSafe(userId, session);
            model.addAttribute("profile", profile);
            model.addAttribute("vouchers", customerPortalService.getActiveVouchers());
            model.addAttribute("profileForm", profile != null ? UpdateProfileRequest.builder()
                    .firstName(profile.getFirstName())
                    .lastName(profile.getLastName())
                    .phone(profile.getPhone())
                    .gender(profile.getGender())
                    .dateOfBirth(profile.getDateOfBirth())
                    .address(profile.getAddress())
                    .avatarUrl(profile.getAvatarUrl())
                    .build() : new UpdateProfileRequest());
            model.addAttribute("errorMessage", "Please review the password fields.");
            return "customer/profile";
        }

        try {
            customerPortalService.changePassword(userId, form);
            redirectAttributes.addFlashAttribute("successMessage", "Your password has been changed successfully.");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/profile";
    }

    // =====================================================================
    // 2. ONLINE TABLE RESERVATIONS (/customer/reservations)
    // =====================================================================

    @GetMapping("/customer/reservations")
    public String reservationsPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        CustomerProfileResponse profile = loadProfileSafe(userId, session);

        model.addAttribute("tables", customerPortalService.getBookableTables());
        model.addAttribute("reservations", customerPortalService.getCustomerReservations(userId));

        if (!model.containsAttribute("reservationForm")) {
            model.addAttribute("reservationForm", CreateReservationRequest.builder()
                    .reservationDate(LocalDate.now())
                    .reservationTime(LocalTime.of(19, 0))
                    .guestCount(2)
                    .customerPhone(profile != null && profile.getPhone() != null ? profile.getPhone() : "0901000002")
                    .build());
        }

        return "customer/reservations";
    }

    @PostMapping("/customer/reservations/create")
    public String createReservation(@Valid @ModelAttribute("reservationForm") CreateReservationRequest form,
                                    BindingResult errors,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (errors.hasErrors()) {
            model.addAttribute("tables", customerPortalService.getBookableTables());
            model.addAttribute("reservations", customerPortalService.getCustomerReservations(userId));
            model.addAttribute("errorMessage", "Please check the reservation details below.");
            return "customer/reservations";
        }

        try {
            CustomerReservationResponse created = customerPortalService.createReservation(userId, form);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Table reservation confirmed! Booking Reference: #" + created.getReservationCode()
                            + " (" + created.getTableCode() + " from " + created.getReservationTime() + " to " + created.getEndTime() + ").");
            return "redirect:/customer/reservations";
        } catch (AppException e) {
            model.addAttribute("tables", customerPortalService.getBookableTables());
            model.addAttribute("reservations", customerPortalService.getCustomerReservations(userId));
            model.addAttribute("errorMessage", e.getMessage());
            return "customer/reservations";
        }
    }

    @PostMapping("/customer/reservations/{id}/cancel")
    public String cancelReservation(@PathVariable("id") Integer reservationId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        try {
            customerPortalService.cancelReservation(userId, reservationId);
            redirectAttributes.addFlashAttribute("successMessage", "Reservation has been cancelled.");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/reservations";
    }

    // =====================================================================
    // 3. PRE-ORDER / TAKEAWAY & REVIEWS (/customer/orders)
    // =====================================================================

    @GetMapping("/customer/orders")
    public String ordersPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        populateOrdersModel(userId, model);

        if (!model.containsAttribute("orderForm")) {
            model.addAttribute("orderForm", CreateOrderRequest.builder()
                    .quantity(1)
                    .orderType("PRE_ORDER")
                    .build());
        }
        if (!model.containsAttribute("reviewForm")) {
            model.addAttribute("reviewForm", CreateReviewRequest.builder()
                    .rating(5)
                    .build());
        }

        return "customer/orders";
    }

    @PostMapping("/customer/orders/create")
    public String createOrder(@Valid @ModelAttribute("orderForm") CreateOrderRequest form,
                              BindingResult errors,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (errors.hasErrors()) {
            populateOrdersModel(userId, model);
            model.addAttribute("reviewForm", CreateReviewRequest.builder().rating(5).build());
            model.addAttribute("errorMessage", "Please review your order details.");
            return "customer/orders";
        }

        try {
            CustomerOrderResponse created = customerPortalService.createOrder(userId, form);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Order #" + created.getOrderCode() + " placed successfully! Total: " + created.getTotalAmount() + " VND.");
            return "redirect:/customer/orders";
        } catch (AppException e) {
            populateOrdersModel(userId, model);
            model.addAttribute("reviewForm", CreateReviewRequest.builder().rating(5).build());
            model.addAttribute("errorMessage", e.getMessage());
            return "customer/orders";
        }
    }

    @PostMapping("/customer/reviews/create")
    public String createReview(@Valid @ModelAttribute("reviewForm") CreateReviewRequest form,
                               BindingResult errors,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (errors.hasErrors()) {
            populateOrdersModel(userId, model);
            model.addAttribute("orderForm", CreateOrderRequest.builder().quantity(1).orderType("PRE_ORDER").build());
            model.addAttribute("errorMessage", "Please enter a rating and comment for your review.");
            return "customer/orders";
        }

        try {
            customerPortalService.createReview(userId, form);
            redirectAttributes.addFlashAttribute("successMessage", "Thank you for sharing your dining review!");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/orders";
    }

    // =====================================================================
    // 4. PROTECTED CUSTOMER REST APIs (/api/customer/**)
    // =====================================================================

    @GetMapping("/api/customer/profile")
    @ResponseBody
    public ApiResponse<CustomerProfileResponse> getCustomerProfileApi(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        return ApiResponse.ok("Customer profile retrieved", loadProfileSafe(userId, session));
    }

    @GetMapping("/api/customer/reservations")
    @ResponseBody
    public ApiResponse<List<CustomerReservationResponse>> getCustomerReservationsApi(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        return ApiResponse.ok("Customer reservations retrieved", customerPortalService.getCustomerReservations(userId));
    }

    @GetMapping("/api/customer/orders")
    @ResponseBody
    public ApiResponse<List<CustomerOrderResponse>> getCustomerOrdersApi(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        return ApiResponse.ok("Customer orders retrieved", customerPortalService.getCustomerOrders(userId));
    }

    private void populateOrdersModel(Integer userId, Model model) {
        model.addAttribute("dishes", customerPortalService.getAvailableDishes());
        model.addAttribute("vouchers", customerPortalService.getActiveVouchers());
        model.addAttribute("reservations", customerPortalService.getCustomerReservations(userId));
        model.addAttribute("orders", customerPortalService.getCustomerOrders(userId));
        model.addAttribute("reviews", customerPortalService.getCustomerReviews(userId));
    }

    private CustomerProfileResponse loadProfileSafe(Integer userId, HttpSession session) {
        try {
            return customerPortalService.getProfile(userId);
        } catch (Exception ignored) {
            AuthUserResponse authUser = (AuthUserResponse) session.getAttribute("authUser");
            if (authUser == null) return null;
            String full = authUser.getFullName() != null ? authUser.getFullName() : "Member";
            String[] parts = full.split(" ", 2);
            return CustomerProfileResponse.builder()
                    .userId(authUser.getUserId())
                    .firstName(parts[0])
                    .lastName(parts.length > 1 ? parts[1] : "")
                    .fullName(full)
                    .email(authUser.getEmail())
                    .phone(authUser.getPhone())
                    .avatarUrl(authUser.getAvatarUrl())
                    .gender("OTHER")
                    .status("ACTIVE")
                    .roleName(authUser.getRoleName())
                    .build();
        }
    }
}
