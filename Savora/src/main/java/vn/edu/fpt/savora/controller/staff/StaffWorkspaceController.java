package vn.edu.fpt.savora.controller.staff;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.fpt.savora.common.ApiResponse;

import java.util.Map;

@Controller
public class StaffWorkspaceController {

    @GetMapping({"/staff", "/staff/workspace"})
    public String workspace(HttpSession session, Model model) {
        model.addAttribute("currentRole", session.getAttribute("roleName"));
        return "staff/workspace";
    }

    @GetMapping("/api/staff/operations")
    @ResponseBody
    public ApiResponse<Map<String, Object>> getStaffOperations(HttpSession session) {
        return ApiResponse.ok("Staff/Manager protected API accessed successfully", Map.of(
                "module", "STAFF_OPERATIONS",
                "allowedRoles", "ADMIN, STAFF/MANAGER, CHEF (roleId = 1, 3, 4)",
                "signedInRole", String.valueOf(session.getAttribute("roleName")),
                "availableTables", 4,
                "occupiedTables", 1,
                "reservedTables", 1
        ));
    }
}
