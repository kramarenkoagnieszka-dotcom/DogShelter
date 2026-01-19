package model;

import java.util.List;
import java.util.ArrayList;

public class ShelterState {
    public List<Dog> dogs = new ArrayList<>();
    public List<User> users = new ArrayList<>();
    public List<Expense> expenses = new ArrayList<>();
    public List<Donation> donations = new ArrayList<>();
    public List<AdoptionApplication> applications = new ArrayList<>();
    public double balance = 0;

    public ShelterState() {}

    public ShelterState(List<Dog> dogs, List<User> users, List<Expense> expenses,
                        List<Donation> donations, List<AdoptionApplication> applications,
                        double balance) {
        this.dogs = dogs;
        this.users = users;
        this.expenses = expenses;
        this.donations = donations;
        this.applications = applications;
        this.balance = balance;
    }
}
