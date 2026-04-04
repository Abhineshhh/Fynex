package org.abhinesh.fynex.controller;

import lombok.RequiredArgsConstructor;
import org.abhinesh.fynex.dto.*;
import org.abhinesh.fynex.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashboardService dashboardService;

    // Full Summary - Analyst and Admin
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard summary fetched", dashboardService.getSummary()));
    }

    // Total income — ANALYST and ADMIN
    @GetMapping("/income")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalIncome() {
        return ResponseEntity.ok(ApiResponse.success("Total income fetched", dashboardService.getTotalIncome()));
    }

    // Total expenses — ANALYST and ADMIN
    @GetMapping("/expenses")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalExpenses() {
        return ResponseEntity.ok(ApiResponse.success("Total expenses fetched",
                dashboardService.getTotalExpenses()));
    }

    // Net balance — ANALYST and ADMIN
    @GetMapping("/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<BigDecimal>> getNetBalance() {
        return ResponseEntity.ok(ApiResponse.success("Net balance fetched",
                dashboardService.getNetBalance()));
    }

    // Category wise totals — ANALYST and ADMIN
    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<List<CategorySummary>>> getCategoryWiseTotals() {
        return ResponseEntity.ok(ApiResponse.success("Category totals fetched",
                dashboardService.getCategoryWiseTotals()));
    }

    // Recent transactions — ALL roles
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<ApiResponse<List<FinancialRecordResponse>>> getRecentTransactions() {
        return ResponseEntity.ok(ApiResponse.success("Recent transactions fetched",
                dashboardService.getRecentTransactions()));
    }

    // Monthly trends — ANALYST and ADMIN
    @GetMapping("/trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<ApiResponse<List<MonthlyTrendyResponse>>> getMonthlyTrends() {
        return ResponseEntity.ok(ApiResponse.success("Monthly trends fetched",
                dashboardService.getMonthlyTrends()));
    }
}
