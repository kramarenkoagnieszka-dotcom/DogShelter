package UI;

import model.DogProfile;
import model.Staff;
import service.FinancialService;
import service.Shelter;
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
                4. Manage Expenses
                5. Logout
                Choice:\s""");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleAddDog();
                case "2" -> handleBrowseDogs();
                case "3" -> handleSearchDogs();
                case "4" -> manageExpensesMenu(staff); // Przejście do podmenu
                case "5" -> active = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }
    private void manageExpensesMenu(Staff staff) {
        boolean back = false;
        while (!back) {
            System.out.println("""
                
                --- EXPENSE MANAGEMENT ---
                1. Add New Expense
                2. View My Expenses
                3. View Expenses by Dog ID
                4. Back to Main Menu
                Choice:\s""");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleAddExpense(staff);
                case "2" -> handleViewMyExpenses(staff);
                case "3" -> handleViewExpensesByDog();
                case "4" -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleViewMyExpenses(Staff staff) {
        System.out.println("\n--- MY RECORDED EXPENSES ---");
        var myExpenses = financialService.getAllExpenses().stream()
                .filter(e -> e.getStaffId() == staff.getId())
                .toList();

        displayExpenses(myExpenses);
    }

    private void handleViewExpensesByDog() {
        try {
            System.out.print("\nEnter Dog ID: ");
            int dogId = Integer.parseInt(scanner.nextLine());
            System.out.println("--- EXPENSES FOR DOG ID: " + dogId + " ---");

            var dogExpenses = financialService.getAllExpenses().stream()
                    .filter(e -> e.getDogId() == dogId)
                    .toList();

            displayExpenses(dogExpenses);
        } catch (NumberFormatException e) {
            System.out.println("Error: Dog ID must be a number.");
        }
    }

    private void displayExpenses(java.util.List<model.Expense> expenses) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
        } else {
            expenses.forEach(System.out::println);
            double total = expenses.stream().mapToDouble(model.Expense::getAmount).sum();
            System.out.printf("TOTAL: %.2f EUR\n", total);
        }
    }

    private void handleAddDog() {
        try {
            System.out.println("\n--- ADD NEW DOG TO SHELTER ---");
            System.out.print("Name: "); String name = scanner.nextLine();
            System.out.print("Breed: "); String breed = scanner.nextLine();
            System.out.print("Age: "); int age = Integer.parseInt(scanner.nextLine());

            System.out.println("\n--- CONFIGURE DOG PROFILE ---");
            System.out.print("Energy Level (1-5): "); int energy = Integer.parseInt(scanner.nextLine());

            boolean garden = askYesNo("Needs Garden?");
            boolean cats = askYesNo("Good with Cats?");
            boolean dogs = askYesNo("Good with Dogs?");
            boolean kids = askYesNo("Good with Kids?");
            boolean medical = askYesNo("Special Medical Needs?");
            boolean behavioral = askYesNo("Special Behavioral Needs?");

            DogProfile profile = new DogProfile(energy, garden, cats, dogs, kids, medical, behavioral);

            shelter.createAndAddDog(name, breed, age, profile);

            System.out.println("\nSuccess: Dog '" + name + "' added to shelter.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers for age and energy level.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleAddExpense(Staff staff) {
        try {
            System.out.println("\n--- RECORD SHELTER EXPENSE ---");
            System.out.print("Enter Dog ID for this expense: ");
            int dogId = Integer.parseInt(scanner.nextLine());

            System.out.print("Expense Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Description: ");
            String description = scanner.nextLine();


            financialService.registerExpense(staff, dogId, amount, description);

            System.out.println("Success: Expense recorded.");
            System.out.printf("Current shelter balance: %.2f PLN\n", financialService.getBalance());

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Operation failed: " + e.getMessage());
        }
    }

    private boolean askYesNo(String message) {
        System.out.print(message + " (y/n): ");
        return scanner.nextLine().equalsIgnoreCase("y");
    }
}