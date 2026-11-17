package vn.edu.fpt.savora.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardSummaryResponse {
    private BigDecimal totalRevenue;
    private long totalOrders;
    private long totalReservations;
    private long availableTables;
    private long totalTables;
    private long availableDishes;
    private long totalDishes;
    private long activeVouchers;
    private long totalUsers;
}
