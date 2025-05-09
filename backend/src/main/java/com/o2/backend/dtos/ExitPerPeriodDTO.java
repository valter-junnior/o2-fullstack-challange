package com.o2.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExitPerPeriodDTO {
    private LocalDate date;
    private BigDecimal total;

    public ExitPerPeriodDTO(java.sql.Date date, BigDecimal total) {
        this.date = date.toLocalDate();
        this.total = total;
    }
}