package main.java.UI;

import main.java.model.Dog;
import main.java.model.DogProfile;
import main.java.model.Expense;
import main.java.model.Staff;
import main.java.service.FinancialService;
import main.java.service.Shelter;
import java.util.Scanner;

public class StaffUI extends BaseUI {
    private final FinancialService financialService;

    public StaffUI(Scanner scanner, Shelter shelter, FinancialService financialService) {
        super(scanner, shelter);
        this.financialService = financialService;
    }

    public void showMenu(Staff staff) {
        boolean active = true;
        while (active) {
            System.out.println("""
                
                --- STAFF PANEL ---
                1. Add New Dog
                2. List All Dogs
                3. Search Dogs
                4. Add Shelter Expense
                5. Logout
                Choice:\s""");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleAddDog();
                case "2" -> handleBrowseDogs(); // Metoda z BaseUI
                case "3" -> handleSearchDogs(); // Metoda z BaseUI
                case "4" -> handleAddExpense(staff);
                case "5" -> active = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleAddDog() {
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

    private void handleAddExpense(Staff staff) {
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

            int transactionId = financialService.getAllExpenses().size() + 5000;
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
}