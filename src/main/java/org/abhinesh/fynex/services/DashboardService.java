package org.abhinesh.fynex.services;

import lombok.RequiredArgsConstructor;
import org.abhinesh.fynex.dto.CategorySummary;
import org.abhinesh.fynex.dto.DashboardSummaryResponse;
import org.abhinesh.fynex.dto.FinancialRecordResponse;
import org.abhinesh.fynex.dto.MonthlyTrendyResponse;
import org.abhinesh.fynex.entity.FinancialRecord;
import org.abhinesh.fynex.repository.FinancialRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialRecordRepository recordRepository;

    // Full summary
    public DashboardSummaryResponse getSummary(){
        BigDecimal totalIncome = recordRepository.getTotalIncome();
        BigDecimal totalExpense = recordRepository.getTotalExpenses();
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        List<CategorySummary> categoryTotals = getCategoryWiseTotals();
        List<FinancialRecordResponse> recentTransactions = getRecentTransactions();

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(netBalance)
                .categoryWiseTotals(categoryTotals)
                .recentTransactions(recentTransactions)
                .build();
    }

    // Total Income
    public BigDecimal getTotalIncome(){
        return recordRepository.getTotalIncome();
    }

    // Total Expense
    public BigDecimal getTotalExpenses(){
        return recordRepository.getTotalExpenses();
    }

    // Net balance
    public BigDecimal getNetBalance(){
        return recordRepository.getTotalIncome()
                .subtract(recordRepository.getTotalExpenses());
    }

    // Category Wise Totals
    public List<CategorySummary> getCategoryWiseTotals() {
        List<Object[]> results = recordRepository.getCategoryWiseTotals();

        return results.stream()
                .map( row -> new CategorySummary(
                        (String ) row[0],
                        (BigDecimal) row[1]
                ))
                .collect(Collectors.toList());
    }

    // Recent Transactions
    public List<FinancialRecordResponse> getRecentTransactions() {
        List<FinancialRecord>  records = recordRepository.findTop5ByDeletedFalseOrderByCreatedAtDesc();

        return records.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Monthly Trends
    public List<MonthlyTrendyResponse> getMonthlyTrends(){
        List<Object[]> results = recordRepository.getMonthlyTrends();

        return results.stream()
                .map(row -> new MonthlyTrendyResponse(
                        ((Number) row[1]).intValue(),
                        ((Number) row[0]).intValue(),
                        row[2].toString(),
                        (BigDecimal) row[3]
                ))
                .collect(Collectors.toList());
    }

    // Private helpers
    private FinancialRecordResponse mapToResponse(FinancialRecord record){
        return FinancialRecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .notes(record.getNotes())
                .createdBy(record.getCreatedBy() != null
                        ? record.getCreatedBy().getUsername() : null)
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}
