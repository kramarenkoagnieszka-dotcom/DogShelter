package main.java.model;

import java.time.LocalDate;

public class Donation extends FinancialTransaction {
    private final int donorId;

    public Donation(int id, double amount, LocalDate date, int donorId) {
        super(id, amount, date);
        this.donorId = donorId;
    }

    public int getDonorId() {
        return donorId;
    }
}