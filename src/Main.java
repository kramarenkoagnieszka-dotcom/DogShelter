//pamiętaj o obsłudze wyjątków
import model.*;
import service.*;

import java.util.ArrayList;
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
            System.out.println("User registered successfully with ID: " + nextId);

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

        switch (choice) {
            case "1" -> handleAddDog();
            case "2" -> shelter.getDogs().forEach(System.out::println);
            case "3" -> handleSearchDogs();
            case "4" -> handleAddExpense(staff);
            default -> System.out.println("Invalid choice.");
        }
        return true;
    }
    private static void handleAddDog() {
        try {
            System.out.println("\n--- ADD NEW DOG TO SHELTER ---");

            int nextId = shelter.getDogs().stream()
                    .mapToInt(Dog::getId)
                    .max()
                    .orElse(0) + 1;

            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Breed: ");
            String breed = scanner.nextLine();
            System.out.print("Age: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.println("\n--- CONFIGURE DOG PROFILE ---");
            System.out.print("Energy Level (1-5): ");
            int energy = Integer.parseInt(scanner.nextLine());

            System.out.print("Needs Garden? (y/n): ");
            boolean garden = scanner.nextLine().equalsIgnoreCase("y");

            System.out.print("Good with Cats? (y/n): ");
            boolean cats = scanner.nextLine().equalsIgnoreCase("y");

            System.out.print("Good with Dogs? (y/n): ");
            boolean dogs = scanner.nextLine().equalsIgnoreCase("y");

            System.out.print("Good with Kids? (y/n): ");
            boolean kids = scanner.nextLine().equalsIgnoreCase("y");

            System.out.print("Special Medical Needs? (y/n): ");
            boolean medical = scanner.nextLine().equalsIgnoreCase("y");

            System.out.print("Special Behavioral Needs? (y/n): ");
            boolean behavioral = scanner.nextLine().equalsIgnoreCase("y");

            DogProfile profile = new DogProfile(energy, garden, cats, dogs, kids, medical, behavioral);

            Dog newDog = new Dog(nextId, name, age, breed, profile);

            shelter.addDog(newDog);
            System.out.println("\nSuccess: Dog '" + name + "' added with ID: " + nextId);

        } catch (NumberFormatException e) {
            System.out.println("Error: Age and Energy Level must be numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("Shelter Error: " + e.getMessage());
        }
    }
    private static void handleSearchDogs() {
        System.out.print("Search by ID, Name or Breed: ");
        String query = scanner.nextLine();

        if (query.isBlank()) {
            System.out.println("Search query cannot be empty.");
            return;
        }

        List<Dog> foundDogs = shelter.searchDogs(query);

        if (foundDogs.isEmpty()) {
            System.out.println("No dogs found matching: " + query);
        } else {
            System.out.println("\n--- SEARCH RESULTS ---");
            foundDogs.forEach(dog -> {
                System.out.println(dog);
                System.out.println("-----------------------");
            });
        }
    }

    private static void handleAddExpense(Staff staff) {
        try {
            System.out.println("\n--- RECORD SHELTER EXPENSE ---");

            System.out.print("Enter Dog ID for this expense: ");
            int dogId = Integer.parseInt(scanner.nextLine());

            Dog dog = shelter.findDogById(dogId)
                    .orElseThrow(() -> new IllegalArgumentException("Dog with ID " + dogId + " not found."));

            System.out.print("Expense Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Description: ");
            String description = scanner.nextLine();

            int transactionId = financialService.getAllExpenses().size() + 5000; // Unikalne ID transakcji
            java.time.LocalDate today = java.time.LocalDate.now();


            Expense newExpense = new Expense(
                    transactionId,
                    amount,
                    today,
                    description,
                    dog.getId(),
                    staff.getId()
            );

            financialService.addExpense(newExpense);

            System.out.println("Success: Expense recorded.");
            System.out.printf("New balance: %.2f PLN\n", financialService.getBalance());

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format for ID or amount.");
        } catch (IllegalArgumentException e) {
            System.out.println("Operation failed: " + e.getMessage());
        }
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

        switch (choice) {
            case "1" -> shelter.getDogs().forEach(System.out::println);
            case "2" -> handleSearchDogs();
            case "3" -> handleMakeDonation(donor);
            case "4" -> handleShowDonationHistory(donor);
            default -> System.out.println("Invalid choice.");
        }
        return true;
    }

    private static void handleMakeDonation(Donor donor) {
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
        }
    }

    private static void handleShowDonationHistory(Donor donor) {
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

