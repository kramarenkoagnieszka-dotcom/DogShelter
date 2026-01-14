//pamiętaj o obsłudze wyjątków
import model.*;
import service.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static Shelter shelter;
    private static AdoptionService adoptionService;
    private static FinancialService financialService;


    public static void main(String[] args) {
        initializeServices();

        System.out.println("Welcome to Shelter Management System");

        while (true) {
            try {
                System.out.println("""
                
                --- WELCOME TO SHELTER SYSTEM ---
                1. Login
                2. Register
                3. Exit
                Choice:\s""");

                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    System.out.println("\n--- LOGIN ---");
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    User user = userService.login(username, password);
                    handleUserSession(user);

                } else if (choice.equals("2")) {
                    handleAddNewUser();

                } else if (choice.equals("3")) {
                    System.out.println("Closing system. Goodbye!");
                    System.exit(0);

                } else {
                    System.out.println("Invalid choice.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handleUserSession(User user) {
        System.out.println("\nLogged in as: " + user.getFirstName() + " (" + user.getClass().getSimpleName() + ")");

        boolean loggedIn = true;
        while (loggedIn) {
            if (user instanceof Admin) {
                loggedIn = showAdminMenu((Admin) user);
            } else if (user instanceof Staff) {
                loggedIn = showStaffMenu((Staff) user);
            } else if (user instanceof Adopter) {
                loggedIn = showAdopterMenu((Adopter) user);
            }
        }
    }

    private static boolean showAdminMenu(Admin admin) {
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
            case "4" -> { return false; }
            default -> System.out.println("Invalid choice.");
        }
        return true;
    }
    private static void handleManageUsers() {
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

    private static void handleAddNewUser() {
        try {
            System.out.println("\n--- REGISTER NEW USER ---");
            System.out.print("Enter ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Role (1: Staff, 2: Adopter, 3: Donor, 4: Admin): ");
            String roleChoice = scanner.nextLine();

            System.out.print("First Name: "); String fn = scanner.nextLine();
            System.out.print("Last Name: "); String ln = scanner.nextLine();
            System.out.print("Username: "); String un = scanner.nextLine();
            System.out.print("Password: "); String pw = scanner.nextLine();
            System.out.print("Email: "); String em = scanner.nextLine();

            User newUser = switch (roleChoice) {
                case "1" -> new Staff(id, fn, ln, un, pw, em);
                case "2" -> new Adopter(id, fn, ln, un, pw, em);
                case "3" -> new Donor(id, fn, ln, un, pw, em);
                case "4" -> new Admin(id, fn, ln, un, pw, em);
                default -> throw new IllegalArgumentException("Invalid role selected.");
            };

            userService.addUser(newUser);
            System.out.println("User added successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void handleRemoveUser() {
        try {
            System.out.print("Enter User ID to remove: ");
            int id = Integer.parseInt(scanner.nextLine());

            userService.removeUserById(id);
            System.out.println("User with ID " + id + " has been removed.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void handleManageApplications() {
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
            long appId = Long.parseLong(scanner.nextLine());
            if (appId == 0) return;

            AdoptionApplication app = adoptionService.getApplicationById(appId)
                    .orElseThrow(() -> new IllegalArgumentException("Application with ID " + appId + " not found."));

            System.out.println("\n--- DETAILED VIEW ---");
            System.out.println(app);
            System.out.println("\n[DOG PROFILE]\n" + app.getDog().getProfile());
            System.out.println("\n[ADOPTER PROFILE]\n" +
                    (app.getAdopter().getProfile() != null ? app.getAdopter().getProfile() : "No questionnaire filled."));

            if (app.isProcessed()) {
                System.out.println("\nStatus: " + app.getStatus() + " (Already processed)");
                return;
            }

            System.out.print("\nDecision (1: ACCEPT, 2: REJECT, Any other: SKIP): ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                app.setStatus(AdoptionApplication.ApplicationStatus.ACCEPTED);
                System.out.println("Application accepted.");
            } else if (choice.equals("2")) {
                app.setStatus(AdoptionApplication.ApplicationStatus.REJECTED);
                System.out.println("Application rejected.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void initializeServices() {
        userService = new UserService();
        shelter = new Shelter();
        financialService = new FinancialService(shelter, userService);

        MatchingService matchingService = new MatchingService();
        adoptionService = new AdoptionService(matchingService);
    }
    private static void handleShelterOverview() {
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
            allDogs.forEach(dog -> {
                System.out.printf("- %s (ID: %d) | Breed: %s | Age: %d\n",
                        dog.getName(), dog.getId(), dog.getBreed(), dog.getAge());
            });
        }

        System.out.println("========================================");
        System.out.println("Press Enter to return...");
        scanner.nextLine();
    }


    private static boolean showStaffMenu(Staff staff) {
        System.out.println("""
                
                --- STAFF PANEL ---
                1. Add New Dog
                2. List All Dogs
                3. Search Dogs
                4. Add Shelter Expense
                5. Logout
                Choice:\s""");
        String choice = scanner.nextLine();
        if (choice.equals("5")) return false;
        // staff logic
        return true;
    }

    private static boolean showAdopterMenu(Adopter adopter) {
        System.out.println("""
                
                --- ADOPTER PANEL ---
                1. Browse Available Dogs
                2. Search Dogs
                3. Fill/Update Adoption Questionnaire
                4. Submit Adoption Application
                5. Check My Applications
                6. Logout
                Choice:\s""");
        String choice = scanner.nextLine();
        if (choice.equals("6")) return false;
        // adopter logic
        return true;
    }

    private static boolean showDonorMenu(Donor donor) {
        System.out.println("""
                
                --- DONOR PANEL ---
                1. Browse Our Dogs
                2. Search Dogs
                3. Make a Donation
                4. My Donation History
                5. Logout
                Choice:\s""");

        String choice = scanner.nextLine();
        if (choice.equals("5")) return false;
        // donor logic
        return true;
    }
}

