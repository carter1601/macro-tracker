package com.macrotracker.model;

import java.time.LocalDate;

public class DailySummary {

    private final LocalDate date;
    private final MacroTotals totals;

    public DailySummary(LocalDate date, MacroTotals totals) {
        this.date = date;
        this.totals = totals;
    }

    public LocalDate getDate() {
        return date;
    }

    public MacroTotals getTotals() {
        return totals;
    }
}