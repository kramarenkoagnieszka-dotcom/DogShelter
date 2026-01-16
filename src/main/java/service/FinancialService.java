package service;

import model.Donor;
import model.Expense;
import model.Donation;
import model.Staff;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FinancialService {
    private List<Expense> expenses = new ArrayList<>();
    private List<Donation> donations = new ArrayList<>();
    private double balance = 0;
    private final Shelter shelter;
    private final UserService userService;

    public FinancialService(Shelter shelter, UserService userService) {
        this.shelter = shelter;
        this.userService = userService;
    }

    public void registerExpense(Staff staff, int dogId, double amount, String description) {
        shelter.findDogById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("Dog with ID " + dogId + " not found."));

        int transactionId = expenses.size() + 5000;
        LocalDate today = LocalDate.now();

        Expense newExpense = new Expense(transactionId, amount, today, description, dogId, staff.getId());

        addExpense(newExpense);
    }

    public void registerDonation(Donor donor, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Donation amount must be greater than 0.");
        }

        int nextId = donations.size() + 8000;
        LocalDate today = LocalDate.now();

        Donation newDonation = new Donation(nextId, amount, today, donor.getId());

        addDonation(newDonation);
    }

    public double calculateTotalDonatedBy(Donor donor) {
        return donor.getDonationHistory().stream()
                .mapToDouble(Donation::getAmount)
                .sum();
    }

    public void addExpense(Expense expense) {
        if (expense.getAmount() > balance) {
            throw new IllegalArgumentException("Insufficient funds. Current balance: " + balance + " PLN.");
        }

        expenses.add(expense);
        balance -= expense.getAmount();

        shelter.findDogById(expense.getDogId()).ifPresent(dog -> {
            dog.addExpense(expense);
        });
    }

    public void addDonation(Donation donation) {
        userService.findUserById(donation.getDonorId()).ifPresentOrElse(user -> {
            if (user instanceof Donor donor) {
                donations.add(donation);
                balance += donation.getAmount();
                donor.getDonationHistory().add(donation);
            } else {
                throw new IllegalArgumentException("User with ID " + donation.getDonorId() + " is not a Donor.");
            }
        }, () -> {
            throw new IllegalArgumentException("Donor with ID " + donation.getDonorId() + " not found.");
        });
    }

    public double getBalance() {
        return balance;
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    public List<Donation> getAllDonations() {
        return new ArrayList<>(donations);
    }

    public List<Expense> getExpensesByStaff(int staffId) {
        return expenses.stream()
                .filter(e -> e.getStaffId() == staffId)
                .collect(Collectors.toList());
    }
}