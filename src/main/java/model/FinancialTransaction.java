package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "transactionType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Expense.class, name = "expense"),
        @JsonSubTypes.Type(value = Donation.class, name = "donation")
})

public abstract class FinancialTransaction {
    private int id;
    private double amount;
    private LocalDate date;

    public FinancialTransaction(int id, double amount, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }
    protected FinancialTransaction(){}

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
}