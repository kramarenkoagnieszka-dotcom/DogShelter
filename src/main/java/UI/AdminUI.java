package main.java.UI;

import main.java.model.Admin;
import main.java.model.AdoptionApplication;
import main.java.model.Dog;
import main.java.model.User;
import main.java.model.Staff;
import main.java.model.Adopter;
import main.java.model.Donor;
import main.java.service.AdoptionService;
import main.java.service.FinancialService;
import main.java.service.Shelter;
import main.java.service.UserService;

import java.util.List;
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
            System.out.print("""
                
                --- ADMIN PANEL ---
                1. Manage Users
                2. Manage Adoption Applications
                3. Shelter Overview (Dogs & Finance)
                4. Logout
                Choice:\s""");

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

        System.out.print("""
            
            1. Add New User
            2. Remove User (by ID)
            3. Back
            Choice:\s""");

        String subChoice = scanner.nextLine();
        switch (subChoice) {
            case "1" -> handleAddNewUser();
            case "2" -> handleRemoveUser();
            case "3" -> {}
            default -> System.out.println("Invalid choice.");
        }
    }

    private void handleAddNewUser() {
        try {
            System.out.println("\n--- REGISTER NEW USER ---");

            int nextId = userService.getAllUsers().stream()
                    .mapToInt(User::getId)
                    .max()
                    .orElse(100) + 1;

            System.out.print("Role (1: Staff, 2: Adopter, 3: Donor, 4: Admin): ");
            String roleChoice = scanner.nextLine();

            System.out.print("First Name: "); String fn = scanner.nextLine();
            System.out.print("Last Name: "); String ln = scanner.nextLine();
            System.out.print("Username: "); String un = scanner.nextLine();
            System.out.print("Password: "); String pw = scanner.nextLine();
            System.out.print("Email: "); String em = scanner.nextLine();

            User newUser = switch (roleChoice) {
                case "1" -> new Staff(nextId, fn, ln, un, pw, em);
                case "2" -> new Adopter(nextId, fn, ln, un, pw, em);
                case "3" -> new Donor(nextId, fn, ln, un, pw, em);
                case "4" -> new Admin(nextId, fn, ln, un, pw, em);
                default -> throw new IllegalArgumentException("Invalid role selected.");
            };

            userService.addUser(newUser);
            System.out.println("User added successfully with ID: " + nextId);
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void handleRemoveUser() {
        try {
            System.out.print("Enter User ID to remove: ");
            int id = Integer.parseInt(scanner.nextLine());
            userService.removeUserById(id);
            System.out.println("User with ID " + id + " has been removed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleManageApplications() {
        System.out.println("\n--- ADOPTION APPLICATIONS MANAGEMENT ---");
        var applications = adoptionService.getAllApplications();

        if (applications.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

        applications.forEach(app -> System.out.printf("[%d] Dog: %s | Adopter: %s | Status: %s\n",
                app.getId(), app.getDog().getName(), app.getAdopter().getLastName(), app.getStatus()));

        System.out.print("\nEnter Application ID (0 to back): ");
        try {
            int appId = Integer.parseInt(scanner.nextLine());
            if (appId == 0) return;

            AdoptionApplication app = adoptionService.getApplicationById(appId)
                    .orElseThrow(() -> new IllegalArgumentException("Application with ID " + appId + " not found."));

            System.out.println("\n--- DETAILED VIEW ---");
            System.out.println(app);

            if (app.isProcessed()) {
                System.out.println("\nStatus: " + app.getStatus() + " (Already processed)");
                return;
            }

            System.out.print("\nDecision (1: ACCEPT, 2: REJECT, Any other: SKIP): ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                adoptionService.changeStatus(appId, AdoptionApplication.ApplicationStatus.ACCEPTED);
                System.out.println("Application accepted.");
            } else if (choice.equals("2")) {
                adoptionService.changeStatus(appId, AdoptionApplication.ApplicationStatus.REJECTED);
                System.out.println("Application rejected.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleShelterOverview() {
        System.out.println("""
            
            ========================================
                     SHELTER GLOBAL OVERVIEW
            ========================================""");

        System.out.printf("Current Balance: %.2f PLN\n", financialService.getBalance());
        System.out.println("Total Donations: " + financialService.getAllDonations().size());
        System.out.println("Total Expenses: " + financialService.getAllExpenses().size());
        System.out.println("----------------------------------------");

        List<Dog> allDogs = shelter.getDogs();
        System.out.println("DOGS STATUS (" + allDogs.size() + " total):");

        if (allDogs.isEmpty()) {
            System.out.println("Shelter is empty.");
        } else {
            allDogs.forEach(dog -> System.out.printf("- %s (ID: %d) | Breed: %s | Age: %d\n",
                    dog.getName(), dog.getId(), dog.getBreed(), dog.getAge()));
        }

        System.out.println("========================================");
        System.out.println("Press Enter to return...");
        scanner.nextLine();
    }
}