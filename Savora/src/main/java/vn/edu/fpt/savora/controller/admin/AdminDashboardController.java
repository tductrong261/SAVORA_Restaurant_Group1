package vn.edu.fpt.savora.controller.admin;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.savora.common.ApiResponse;
import vn.edu.fpt.savora.dto.admin.request.AdminCreateUserRequest;
import vn.edu.fpt.savora.dto.admin.request.AdminDishRequest;
import vn.edu.fpt.savora.dto.admin.request.AdminTableRequest;
import vn.edu.fpt.savora.dto.admin.request.AdminVoucherRequest;
import vn.edu.fpt.savora.dto.admin.response.AdminDashboardSummaryResponse;
import vn.edu.fpt.savora.dto.admin.response.AdminDishResponse;
import vn.edu.fpt.savora.dto.admin.response.AdminTableResponse;
import vn.edu.fpt.savora.dto.admin.response.AdminUserResponse;
import vn.edu.fpt.savora.exception.AppException;
import vn.edu.fpt.savora.service.admin.AdminManagementService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminManagementService adminService;

    // =====================================================================
    // 1. EXECUTIVE DASHBOARD & ANALYTICS (/admin/dashboard)
    // =====================================================================

    @GetMapping({"/admin", "/admin/dashboard"})
    public String dashboardPage(Model model) {
        model.addAttribute("summary", adminService.getDashboardSummary());
        model.addAttribute("recentReservations", adminService.getRecentReservations());
        model.addAttribute("recentOrders", adminService.getRecentOrders());
        return "admin/dashboard";
    }

    // =====================================================================
    // 2. PERSONNEL & ROLE GOVERNANCE (/admin/users)
    // =====================================================================

    @GetMapping("/admin/users")
    public String usersPage(Model model) {
        model.addAttribute("summary", adminService.getDashboardSummary());
        model.addAttribute("users", adminService.getAllUsers());
        if (!model.containsAttribute("createUserForm")) {
            model.addAttribute("createUserForm", AdminCreateUserRequest.builder()
                    .roleName("STAFF")
                    .gender("MALE")
                    .build());
        }
        return "admin/users";
    }

    @PostMapping("/admin/users/create")
    public String createUser(@Valid @ModelAttribute("createUserForm") AdminCreateUserRequest form,
                             BindingResult errors,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute("summary", adminService.getDashboardSummary());
            model.addAttribute("users", adminService.getAllUsers());
            model.addAttribute("errorMessage", "Please review the highlighted account fields.");
            return "admin/users";
        }
        try {
            AdminUserResponse created = adminService.createUser(form);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Account created for " + created.getFullName() + " (" + created.getRoleName() + ").");
            return "redirect:/admin/users";
        } catch (AppException e) {
            model.addAttribute("summary", adminService.getDashboardSummary());
            model.addAttribute("users", adminService.getAllUsers());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/users";
        }
    }

    @PostMapping("/admin/users/{id}/role")
    public String updateUserRole(@PathVariable("id") Integer userId,
                                 @RequestParam("roleName") String roleName,
                                 RedirectAttributes redirectAttributes) {
        try {
            adminService.updateUserRole(userId, roleName);
            redirectAttributes.addFlashAttribute("successMessage", "Updated account #" + userId + " role to " + roleName + ".");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable("id") Integer userId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Integer currentAdminId = (Integer) session.getAttribute("userId");
        try {
            adminService.toggleUserLockStatus(userId, currentAdminId);
            redirectAttributes.addFlashAttribute("successMessage", "Updated account #" + userId + " lock status.");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot lock your own active administrator account.");
        }
        return "redirect:/admin/users";
    }

    // =====================================================================
    // 3. CULINARY MENU & CATEGORY MANAGEMENT (/admin/menu)
    // =====================================================================

    @GetMapping("/admin/menu")
    public String menuPage(Model model) {
        model.addAttribute("summary", adminService.getDashboardSummary());
        model.addAttribute("categories", adminService.getAllCategories());
        model.addAttribute("dishes", adminService.getAllDishes());
        if (!model.containsAttribute("dishForm")) {
            model.addAttribute("dishForm", AdminDishRequest.builder()
                    .preparationTimeMinutes(15)
                    .isSignature(false)
                    .status("AVAILABLE")
                    .build());
        }
        return "admin/menu";
    }

    @PostMapping("/admin/menu/create")
    public String createDish(@Valid @ModelAttribute("dishForm") AdminDishRequest form,
                             BindingResult errors,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute("summary", adminService.getDashboardSummary());
            model.addAttribute("categories", adminService.getAllCategories());
            model.addAttribute("dishes", adminService.getAllDishes());
            model.addAttribute("errorMessage", "Please review the dish details.");
            return "admin/menu";
        }
        try {
            AdminDishResponse created = adminService.createDish(form);
            redirectAttributes.addFlashAttribute("successMessage", "Added dish \"" + created.getDishName() + "\" to Savora menu.");
            return "redirect:/admin/menu";
        } catch (AppException e) {
            model.addAttribute("summary", adminService.getDashboardSummary());
            model.addAttribute("categories", adminService.getAllCategories());
            model.addAttribute("dishes", adminService.getAllDishes());
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/menu";
        }
    }

    @PostMapping("/admin/menu/{id}/status")
    public String updateDishStatus(@PathVariable("id") Integer dishId,
                                   @RequestParam("status") String status,
                                   RedirectAttributes redirectAttributes) {
        try {
            adminService.updateDishStatus(dishId, status);
            redirectAttributes.addFlashAttribute("successMessage", "Updated dish #" + dishId + " status to " + status + ".");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/menu";
    }

    @PostMapping("/admin/menu/{id}/signature")
    public String toggleDishSignature(@PathVariable("id") Integer dishId,
                                      RedirectAttributes redirectAttributes) {
        try {
            adminService.toggleDishSignature(dishId);
            redirectAttributes.addFlashAttribute("successMessage", "Updated Chef's Signature highlight for dish #" + dishId + ".");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/menu";
    }

    // =====================================================================
    // 4. FLOOR TABLES & VOUCHER GOVERNANCE (/admin/tables)
    // =====================================================================

    @GetMapping("/admin/tables")
    public String tablesPage(Model model) {
        model.addAttribute("summary", adminService.getDashboardSummary());
        model.addAttribute("tables", adminService.getAllTables());
        model.addAttribute("vouchers", adminService.getAllVouchers());

        if (!model.containsAttribute("tableForm")) {
            model.addAttribute("tableForm", AdminTableRequest.builder()
                    .area("Tầng 1")
                    .capacity(4)
                    .status("AVAILABLE")
                    .build());
        }
        if (!model.containsAttribute("voucherForm")) {
            model.addAttribute("voucherForm", AdminVoucherRequest.builder()
                    .discountPercent(10)
                    .maxDiscountAmount(new BigDecimal("100000"))
                    .minOrderValue(new BigDecimal("500000"))
                    .quantity(50)
                    .build());
        }
        return "admin/tables";
    }

    @PostMapping("/admin/tables/create")
    public String createTable(@Valid @ModelAttribute("tableForm") AdminTableRequest form,
                              BindingResult errors,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute("summary", adminService.getDashboardSummary());
            model.addAttribute("tables", adminService.getAllTables());
            model.addAttribute("vouchers", adminService.getAllVouchers());
            model.addAttribute("voucherForm", AdminVoucherRequest.builder().discountPercent(10).quantity(50).build());
            model.addAttribute("errorMessage", "Please review the dining table fields.");
            return "admin/tables";
        }
        try {
            AdminTableResponse created = adminService.createTable(form);
            redirectAttributes.addFlashAttribute("successMessage", "Created dining table " + created.getTableCode() + ".");
            return "redirect:/admin/tables";
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Table code already exists or is invalid.");
            return "redirect:/admin/tables";
        }
    }

    @PostMapping("/admin/tables/{id}/status")
    public String updateTableStatus(@PathVariable("id") Integer tableId,
                                    @RequestParam("status") String status,
                                    RedirectAttributes redirectAttributes) {
        try {
            adminService.updateTableStatus(tableId, status);
            redirectAttributes.addFlashAttribute("successMessage", "Updated table #" + tableId + " status to " + status + ".");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/tables";
    }

    @PostMapping("/admin/vouchers/create")
    public String createVoucher(@Valid @ModelAttribute("voucherForm") AdminVoucherRequest form,
                                BindingResult errors,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute("summary", adminService.getDashboardSummary());
            model.addAttribute("tables", adminService.getAllTables());
            model.addAttribute("vouchers", adminService.getAllVouchers());
            model.addAttribute("tableForm", AdminTableRequest.builder().area("Tầng 1").capacity(4).status("AVAILABLE").build());
            model.addAttribute("errorMessage", "Please review the promotional voucher fields.");
            return "admin/tables";
        }
        try {
            adminService.createVoucher(form);
            redirectAttributes.addFlashAttribute("successMessage", "Created promotional voucher " + form.getVoucherCode().toUpperCase() + ".");
            return "redirect:/admin/tables";
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Voucher code already exists.");
            return "redirect:/admin/tables";
        }
    }

    @PostMapping("/admin/vouchers/{id}/toggle")
    public String toggleVoucher(@PathVariable("id") Integer voucherId,
                                RedirectAttributes redirectAttributes) {
        try {
            adminService.toggleVoucherStatus(voucherId);
            redirectAttributes.addFlashAttribute("successMessage", "Updated voucher #" + voucherId + " status.");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/tables";
    }

    // =====================================================================
    // 5. PROTECTED ADMIN REST APIs (/api/admin/**)
    // =====================================================================

    @GetMapping("/api/admin/summary")
    @ResponseBody
    public ApiResponse<AdminDashboardSummaryResponse> getAdminSummaryApi() {
        return ApiResponse.ok("Admin summary retrieved", adminService.getDashboardSummary());
    }

    @GetMapping("/api/admin/users")
    @ResponseBody
    public ApiResponse<List<AdminUserResponse>> getAdminUsersApi() {
        return ApiResponse.ok("Admin users retrieved", adminService.getAllUsers());
    }

    @GetMapping("/api/admin/dishes")
    @ResponseBody
    public ApiResponse<List<AdminDishResponse>> getAdminDishesApi() {
        return ApiResponse.ok("Admin dishes retrieved", adminService.getAllDishes());
    }

    @GetMapping("/api/admin/tables")
    @ResponseBody
    public ApiResponse<List<AdminTableResponse>> getAdminTablesApi() {
        return ApiResponse.ok("Admin tables retrieved", adminService.getAllTables());
    }
}
