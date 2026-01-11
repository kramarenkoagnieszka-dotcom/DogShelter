//pamiętaj o obsłudze wyjątków
import model.*;
import service.*;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static Shelter shelter;
    private static AdoptionService adoptionService;
    private static FinancialService financialService;

    public static void main(String[] args) {

        System.out.println("Welcome to Shelter Management System");

        while (true) {
            try {
                System.out.println("\n--- LOGIN ---");
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                User user = userService.login(username, password);
                handleUserSession(user);

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
        System.out.println("""
                
                --- ADMIN PANEL ---
                1. Manage Users (List all)
                2. Manage Adoption Applications
                3. Shelter Overview (Dogs & Finance)
                4. Logout
                Choice:\s""");
        String choice = scanner.nextLine();
        if (choice.equals("4")) return false;
        // admin logic
        return true;
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

