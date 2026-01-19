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
            System.out.println("No applications found.");
            return;
        }
        System.out.println("\nID  | Status   | Match | Dog        | Adopter");
        System.out.println("---------------------------------------------------");
        apps.forEach(app -> System.out.printf("[%d] | %-8s | %3.0f%% | %-10s | %s\n",
                app.getId(),
                app.getStatus(),
                app.getMatchPercentage(),
                app.getDog().getName(),
                app.getAdopter().getLastName()));

        System.out.print("\nEnter ID to view details and process (0 to back): ");
        try {
            int appId = Integer.parseInt(scanner.nextLine());
            if (appId == 0) return;

            AdoptionApplication app = adoptionService.getApplicationById(appId)
                    .orElseThrow(() -> new IllegalArgumentException("Application not found."));

            displayDetailedApplicationView(app);

            System.out.print("\nDecision (1: ACCEPT, 2: REJECT, 0: SKIP): ");
            String decision = scanner.nextLine();

            switch (decision) {
                case "1" -> {
                    adoptionService.changeStatus(appId, AdoptionApplication.ApplicationStatus.ACCEPTED);
                    System.out.println("Application ACCEPTED.");
                }
                case "2" -> {
                    adoptionService.changeStatus(appId, AdoptionApplication.ApplicationStatus.REJECTED);
                    System.out.println("Application REJECTED.");
                }
                default -> System.out.println("Decision skipped.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayDetailedApplicationView(AdoptionApplication app) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("   DETAILED APPLICATION VIEW - ID: " + app.getId());
        System.out.println("=".repeat(50));

        System.out.println("\n[DOG DETAILS]");
        System.out.println("Name: " + app.getDog().getName() + " | Breed: " + app.getDog().getBreed() + " | Age: " + app.getDog().getAge());
        System.out.println("Profile: " + app.getDog().getProfile());

        System.out.println("\n[ADOPTER DETAILS]");
        System.out.println("Name: " + app.getAdopter().getFirstName() + " " + app.getAdopter().getLastName());
        System.out.println("Profile: " + (app.getAdopter().getProfile() != null ? app.getAdopter().getProfile() : "No questionnaire filled."));

        System.out.println("\n[APPLICATION INFO]");
        System.out.printf("Match Score: %.0f%%\n", app.getMatchPercentage());
        System.out.println("Notes: " + (app.getNotes().isEmpty() ? "None" : app.getNotes()));
        System.out.println("=".repeat(50));
    }

    private void handleShelterOverview() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- SHELTER GLOBAL OVERVIEW ---");
            System.out.printf("Current Balance: %.2f PLN\n", financialService.getBalance());
            System.out.println("1. View All Donations");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Expenses by Staff ID");
            System.out.println("4. Back");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    System.out.println("\n--- DONATION HISTORY ---");
                    var donations = financialService.getAllDonations();
                    if (donations.isEmpty()) System.out.println("No donations recorded.");
                    else donations.forEach(System.out::println);
                }
                case "2" -> {
                    System.out.println("\n--- EXPENSE HISTORY ---");
                    var expenses = financialService.getAllExpenses();
                    if (expenses.isEmpty()) System.out.println("No expenses recorded.");
                    else expenses.forEach(System.out::println);
                }
                case "3" -> handleStaffExpenses();
                case "4" -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleStaffExpenses() {
        try {
            System.out.print("Enter Staff ID: ");
            int staffId = Integer.parseInt(scanner.nextLine());

            var staffExpenses = financialService.getExpensesByStaff(staffId);

            System.out.println("\n--- EXPENSES FOR STAFF ID: " + staffId + " ---");
            if (staffExpenses.isEmpty()) {
                System.out.println("No expenses found for this staff member.");
            } else {
                staffExpenses.forEach(System.out::println);
                double total = staffExpenses.stream().mapToDouble(FinancialTransaction::getAmount).sum();
                System.out.printf("Total spent by this employee: %.2f PLN\n", total);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Staff ID must be a number.");
        }
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