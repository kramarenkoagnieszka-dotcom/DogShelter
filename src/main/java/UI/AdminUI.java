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
        var apps = adoptionService.getAllApplications();
        if (apps.isEmpty()) { System.out.println("No apps."); return; }

        apps.forEach(app -> System.out.printf("[%d] Dog: %s | Status: %s\n", app.getId(), app.getDog().getName(), app.getStatus()));

        System.out.print("\nEnter ID to process (0 to back): ");
        try {
            int appId = Integer.parseInt(scanner.nextLine());
            if (appId == 0) return;

            System.out.print("Decision (1: ACCEPT, 2: REJECT): ");
            String decision = scanner.nextLine();

            if (decision.equals("1")) {
                adoptionService.changeStatus(appId, AdoptionApplication.ApplicationStatus.ACCEPTED);
                System.out.println("Accepted.");
            } else if (decision.equals("2")) {
                adoptionService.changeStatus(appId, AdoptionApplication.ApplicationStatus.REJECTED);
                System.out.println("Rejected.");
            }
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