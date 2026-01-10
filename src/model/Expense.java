package model;

import java.time.LocalDate;

public class Expense extends FinancialTransaction {
    private final String description;

    public Expense(int id, double amount, LocalDate date, String description) {
        super(id, amount, date);
        this.description = description;
    }

    public String getDescription() { return description; }
}