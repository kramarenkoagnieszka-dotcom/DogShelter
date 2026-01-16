package UI;

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

            financialService.registerDonation(donor, amount);

            System.out.println("Thank you for your donation of " + amount + " EUR!");
            System.out.println("Your contribution helps us take better care of our dogs.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format. Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Donation failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
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

            double total = financialService.calculateTotalDonatedBy(donor);

            System.out.println("----------------------------------------");
            System.out.printf("TOTAL DONATED: %.2f EUR\n", total);
            System.out.println("----------------------------------------");
        }
    }
}