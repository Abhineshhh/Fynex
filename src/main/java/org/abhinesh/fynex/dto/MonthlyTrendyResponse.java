package org.abhinesh.fynex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyTrendyResponse {
    private int year;
    private int month;
    private String type;
    private BigDecimal total;
}
