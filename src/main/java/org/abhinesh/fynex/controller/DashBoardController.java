package org.abhinesh.fynex.controller;

import lombok.RequiredArgsConstructor;
import org.abhinesh.fynex.dto.CategorySummary;
import org.abhinesh.fynex.dto.DashboardSummaryResponse;
import org.abhinesh.fynex.dto.FinancialRecordResponse;
import org.abhinesh.fynex.dto.MonthlyTrendyResponse;
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
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    // Total income — ANALYST and ADMIN
    @GetMapping("/income")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<BigDecimal> getTotalIncome() {
        return ResponseEntity.ok(dashboardService.getTotalIncome());
    }

    // Total expenses — ANALYST and ADMIN
    @GetMapping("/expenses")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<BigDecimal> getTotalExpenses() {
        return ResponseEntity.ok(dashboardService.getTotalExpenses());
    }

    // Net balance — ANALYST and ADMIN
    @GetMapping("/balance")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<BigDecimal> getNetBalance() {
        return ResponseEntity.ok(dashboardService.getNetBalance());
    }

    // Category wise totals — ANALYST and ADMIN
    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<List<CategorySummary>> getCategoryWiseTotals() {
        return ResponseEntity.ok(dashboardService.getCategoryWiseTotals());
    }

    // Recent transactions — ALL roles
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<List<FinancialRecordResponse>> getRecentTransactions() {
        return ResponseEntity.ok(dashboardService.getRecentTransactions());
    }

    // Monthly trends — ANALYST and ADMIN
    @GetMapping("/trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<List<MonthlyTrendyResponse>> getMonthlyTrends() {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends());
    }
}
