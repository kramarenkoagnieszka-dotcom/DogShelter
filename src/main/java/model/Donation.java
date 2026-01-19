package model;

import java.time.LocalDate;

public class Donation extends FinancialTransaction {
    private int donorId;

    public Donation(int id, double amount, LocalDate date, int donorId) {
        super(id, amount, date);
        this.donorId = donorId;
    }
    private Donation(){}

    public int getDonorId() {
        return donorId;
    }
}