package main.java;

import main.java.model.*;
import main.java.service.*;
import main.java.UI.*;

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
                    handleLogin();
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

    private static void handleLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userService.login(username, password);
        handleUserSession(user);
    }

    private static void handleUserSession(User user) {
        System.out.println("\nLogged in as: " + user.getFirstName() + " (" + user.getClass().getSimpleName() + ")");

        if (user instanceof Admin admin) {
            AdminUI adminUI = new AdminUI(scanner, shelter, userService, adoptionService, financialService);
            adminUI.showMenu(admin);
        } else if (user instanceof Staff staff) {
            StaffUI staffUI = new StaffUI(scanner, shelter, financialService);
            staffUI.showMenu(staff);
        } else if (user instanceof Adopter adopter) {
            AdopterUI adopterUI = new AdopterUI(scanner, shelter, adoptionService);
            adopterUI.showMenu(adopter);
        } else if (user instanceof Donor donor) {
            DonorUI donorUI = new DonorUI(scanner, shelter, financialService);
            donorUI.showMenu(donor);
        } else {
            System.out.println("Unknown user role.");
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
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void initializeServices() {
        userService = new UserService();
        shelter = new Shelter();
        financialService = new FinancialService(shelter, userService);
        MatchingService matchingService = new MatchingService();
        adoptionService = new AdoptionService(matchingService);

        userService.addUser(new Admin(1, "System", "Admin", "admin", "admin", "admin@shelter.com"));
    }
}