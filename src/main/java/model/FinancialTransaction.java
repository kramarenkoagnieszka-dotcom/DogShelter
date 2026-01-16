package main.java.model;

import java.time.LocalDate;

public abstract class FinancialTransaction {
    private final int id;
    private final double amount;
    private final LocalDate date;

    public FinancialTransaction(int id, double amount, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
}