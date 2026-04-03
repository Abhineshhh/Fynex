package org.abhinesh.fynex.repository;

import org.abhinesh.fynex.entity.FinancialRecord;
import org.abhinesh.fynex.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // find all non deleted records
    List<FinancialRecord> findByDeletedFalse();

    // filter by type
    List<FinancialRecord> findByTypeAndDeletedFalse(TransactionType type);

    // filter by category
    List<FinancialRecord> findByCategoryAndDeletedFalse(String category);

    // filter by date range
    List<FinancialRecord> findByDateBetweenAndDeletedFalse(LocalDate start, LocalDate end);

    // filter by type and date range
    List<FinancialRecord> findByTypeAndDateBetweenAndDeletedFalse(TransactionType type, LocalDate start, LocalDate end);

    // Total income
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f " +
            "WHERE f.type = 'INCOME' AND f.deleted = false")
    BigDecimal getTotalIncome();

    // Total expenses
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f " +
            "WHERE f.type = 'EXPENSE' AND f.deleted = false")
    BigDecimal getTotalExpenses();

    // Category wise totals
    @Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f " +
            "WHERE f.deleted = false GROUP BY f.category")
    List<Object[]> getCategoryWiseTotals();

    // Monthly trends
    @Query("SELECT MONTH(f.date), YEAR(f.date), f.type, SUM(f.amount) " +
            "FROM FinancialRecord f WHERE f.deleted = false " +
            "GROUP BY YEAR(f.date), MONTH(f.date), f.type " +
            "ORDER BY YEAR(f.date), MONTH(f.date)")
    List<Object[]> getMonthlyTrends();

    // recent 5 records
    List<FinancialRecord> findTop5ByDeletedFalseOrderByCreatedAtDesc();

}
