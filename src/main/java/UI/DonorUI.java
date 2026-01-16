package UI;

import model.Donation;
import model.Donor;
import service.FinancialService;
import service.Shelter;
import java.util.Scanner;

public class DonorUI extends BaseUI {
    private final FinancialService financialService;

    public DonorUI(Scanner scanner, Shelter shelter, FinancialService financialService) {
        super(scanner, shelter);
        this.financialService = financialService;
    }

    public void showMenu(Donor donor) {
        boolean active = true;
        while (active) {
            System.out.println("""
                
                --- DONOR PANEL ---
                1. Browse Our Dogs
                2. Search Dogs
                3. Make a Donation
                4. My Donation History
                5. Logout
                Choice:\s""");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleBrowseDogs();
                case "2" -> handleSearchDogs();
                case "3" -> handleMakeDonation(donor);
                case "4" -> handleShowDonationHistory(donor);
                case "5" -> active = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleMakeDonation(Donor donor) {
        try {
            System.out.println("\n--- MAKE A DONATION ---");
            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount <= 0) {
                System.out.println("Amount must be greater than 0.");
                return;
            }

            int transactionId = financialService.getAllDonations().size() + 8000;
            java.time.LocalDate today = java.time.LocalDate.now();

            Donation donation = new Donation(transactionId, amount, today, donor.getId());
            financialService.addDonation(donation);

            System.out.println("Thank you for your donation of " + amount + " EUR!");
            System.out.println("Transaction ID: " + transactionId);

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format.");
        } catch (Exception e) {
            System.out.println("Donation failed: " + e.getMessage());
        }
    }

    private void handleShowDonationHistory(Donor donor) {
        System.out.println("\n--- YOUR DONATION HISTORY ---");
        var history = donor.getDonationHistory();

        if (history.isEmpty()) {
            System.out.println("You haven't made any donations yet.");
        } else {
            history.forEach(d -> System.out.printf("[%s] ID: %d | Amount: %.2f EUR\n",
                    d.getDate(), d.getId(), d.getAmount()));

            double total = history.stream().mapToDouble(Donation::getAmount).sum();
            System.out.printf("Total donated: %.2f EUR\n", total);
        }
    }
}