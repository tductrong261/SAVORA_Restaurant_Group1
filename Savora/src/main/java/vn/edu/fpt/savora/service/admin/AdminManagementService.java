package vn.edu.fpt.savora.service.admin;

import vn.edu.fpt.savora.dto.admin.request.AdminCreateUserRequest;
import vn.edu.fpt.savora.dto.admin.request.AdminDishRequest;
import vn.edu.fpt.savora.dto.admin.request.AdminTableRequest;
import vn.edu.fpt.savora.dto.admin.request.AdminVoucherRequest;
import vn.edu.fpt.savora.dto.admin.response.AdminDashboardSummaryResponse;
import vn.edu.fpt.savora.dto.admin.response.AdminDishResponse;
import vn.edu.fpt.savora.dto.admin.response.AdminTableResponse;
import vn.edu.fpt.savora.dto.admin.response.AdminUserResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerOrderResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerReservationResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerVoucherResponse;
import vn.edu.fpt.savora.entity.Category;

import java.util.List;

public interface AdminManagementService {

    AdminDashboardSummaryResponse getDashboardSummary();

    List<CustomerReservationResponse> getRecentReservations();

    List<CustomerOrderResponse> getRecentOrders();

    // 1. User & Role Governance
    List<AdminUserResponse> getAllUsers();

    AdminUserResponse createUser(AdminCreateUserRequest request);

    void updateUserRole(Integer userId, String roleName);

    void toggleUserLockStatus(Integer targetUserId, Integer currentAdminId);

    // 2. Menu & Category Management
    List<Category> getAllCategories();

    List<AdminDishResponse> getAllDishes();

    AdminDishResponse createDish(AdminDishRequest request);

    void updateDishStatus(Integer dishId, String status);

    void toggleDishSignature(Integer dishId);

    // 3. Dining Table & Voucher Management
    List<AdminTableResponse> getAllTables();

    AdminTableResponse createTable(AdminTableRequest request);

    void updateTableStatus(Integer tableId, String status);

    List<CustomerVoucherResponse> getAllVouchers();

    CustomerVoucherResponse createVoucher(AdminVoucherRequest request);

    void toggleVoucherStatus(Integer voucherId);
}
