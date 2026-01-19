package model;

import java.time.LocalDate;

public class Expense extends FinancialTransaction {
    private String description;
    private int dogId;
    private int staffId;

    public Expense(int id, double amount, LocalDate date, String description, int dogId, int staffId) {
        super(id, amount, date);
        this.description = description;
        this.dogId = dogId;
        this.staffId = staffId;
    }
    private Expense(){}

    public String getDescription() { return description; }
    public int getDogId() { return dogId; }
    public int getStaffId() {return staffId; }
}