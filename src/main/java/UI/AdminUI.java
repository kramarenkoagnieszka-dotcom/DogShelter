package UI;

import model.*;
import service.*;
import java.util.Scanner;

public class AdminUI extends BaseUI {
    private final UserService userService;
    private final AdoptionService adoptionService;
    private final FinancialService financialService;

    public AdminUI(Scanner scanner, Shelter shelter, UserService userService,
                   AdoptionService adoptionService, FinancialService financialService) {
        super(scanner, shelter);
        this.userService = userService;
        this.adoptionService = adoptionService;
        this.financialService = financialService;
    }

    public void showMenu(Admin admin) {
        boolean active = true;
        while (active) {
            System.out.print("\n--- ADMIN PANEL ---\n1. Manage Users\n2. Manage Adoption Applications\n3. Shelter Overview\n4. Logout\nChoice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleManageUsers();
                case "2" -> handleManageApplications();
                case "3" -> handleShelterOverview();
                case "4" -> active = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleManageUsers() {
        System.out.println("\n--- USER MANAGEMENT ---");
        userService.getAllUsers().forEach(System.out::println);
        System.out.print("\n1. Add User\n2. Remove User\n3. Back\nChoice: ");
        String subChoice = scanner.nextLine();

        switch (subChoice) {
            case "1" -> handleAddNewUser();
            case "2" -> handleRemoveUser();
        }
    }

    private void handleAddNewUser() {
        try {
            System.out.print("Role (1: Staff, 2: Adopter, 3: Donor, 4: Admin): ");
            String role = scanner.nextLine();
            System.out.print("First Name: "); String fn = scanner.nextLine();
            System.out.print("Last Name: "); String ln = scanner.nextLine();
            System.out.print("Username: "); String un = scanner.nextLine();
            System.out.print("Password: "); String pw = scanner.nextLine();
            System.out.print("Email: "); String em = scanner.nextLine();

            userService.createAndAddUser(role, fn, ln, un, pw, em);
            System.out.println("User added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleManageApplications() {
        System.out.println("\n--- ADOPTION APPLICATIONS ---");
        System.out.println("1. Show All Applications");
        System.out.println("2. Show Only Pending Applications");
        System.out.print("Choice: ");
        String filterChoice = scanner.nextLine();

        var apps = filterChoice.equals("2")
                ? adoptionService.getPendingApplications()
                : adoptionService.getAllApplications();

        if (apps.isEmpty()) {
            System.out.println("No applications found for the selected filter.");
            return;
        }

        apps.forEach(app -> System.out.printf("[%d] Dog: %s | Adopter: %s | Status: %s\n",
                app.getId(), app.getDog().getName(), app.getAdopter().getLastName(), app.getStatus()));

        System.out.print("\nEnter ID to process (0 to back): ");
        try {
            int appId = Integer.parseInt(scanner.nextLine());
            if (appId == 0) return;

            AdoptionApplication app = adoptionService.getApplicationById(appId)
                    .orElseThrow(() -> new IllegalArgumentException("Application with ID " + appId + " not found."));

            System.out.println("\nProcessing: " + app.getDog().getName() + " for " + app.getAdopter().getFirstName());
            System.out.print("Decision (1: ACCEPT, 2: REJECT, 3: SKIP): ");
            String decision = scanner.nextLine();

            switch (decision) {
                case "1" -> {
                    adoptionService.changeStatus(appId, AdoptionApplication.ApplicationStatus.ACCEPTED);
                    System.out.println("Application ACCEPTED. Dog is now marked as adopted.");
                }
                case "2" -> {
                    adoptionService.changeStatus(appId, AdoptionApplication.ApplicationStatus.REJECTED);
                    System.out.println("Application REJECTED.");
                }
                default -> System.out.println("Decision skipped.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid ID format.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleShelterOverview() {
        System.out.println("\n--- OVERVIEW ---");
        System.out.printf("Balance: %.2f PLN\n", financialService.getBalance());
        System.out.println("Dogs in shelter: " + shelter.getDogs().size());
        shelter.getDogs().forEach(d -> System.out.println("- " + d.getName() + " (" + d.getBreed() + ")"));
        System.out.println("\nPress Enter...");
        scanner.nextLine();
    }

    private void handleRemoveUser() {
        try {
            System.out.print("User ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            userService.removeUserById(id);
            System.out.println("Removed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}