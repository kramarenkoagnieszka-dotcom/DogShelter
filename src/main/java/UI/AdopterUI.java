package main.java.UI;

import main.java.model.Adopter;
import main.java.model.AdopterProfile;
import main.java.model.Dog;
import main.java.service.AdoptionService;
import main.java.service.Shelter;
import java.util.Scanner;

public class AdopterUI extends BaseUI {
    private final AdoptionService adoptionService;

    public AdopterUI(Scanner scanner, Shelter shelter, AdoptionService adoptionService) {
        super(scanner, shelter);
        this.adoptionService = adoptionService;
    }

    public void showMenu(Adopter adopter) {
        boolean active = true;
        while (active) {
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
            switch (choice) {
                case "1" -> handleBrowseDogs(); // Metoda z BaseUI
                case "2" -> handleSearchDogs(); // Metoda z BaseUI
                case "3" -> handleUpdateQuestionnaire(adopter);
                case "4" -> handleSubmitApplication(adopter);
                case "5" -> handleShowMyApplications(adopter);
                case "6" -> active = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleUpdateQuestionnaire(Adopter adopter) {
        System.out.println("\n--- ADOPTION QUESTIONNAIRE (DETAILED) ---");
        try {
            System.out.print("Preferred Dog Energy Level (1-5): ");
            int energy = Integer.parseInt(scanner.nextLine());
            System.out.print("Do you have a garden? (y/n): ");
            boolean garden = scanner.nextLine().equalsIgnoreCase("y");
            System.out.print("Do you have cats? (y/n): ");
            boolean cats = scanner.nextLine().equalsIgnoreCase("y");
            System.out.print("Do you have other dogs? (y/n): ");
            boolean dogs = scanner.nextLine().equalsIgnoreCase("y");
            System.out.print("Do you have children? (y/n): ");
            boolean kids = scanner.nextLine().equalsIgnoreCase("y");

            System.out.print("Monthly budget for dog care (PLN): ");
            double budget = Double.parseDouble(scanner.nextLine());
            System.out.print("Are you willing to adopt a disabled dog? (y/n): ");
            boolean disabled = scanner.nextLine().equalsIgnoreCase("y");

            System.out.print("Have you had a dog before? (y/n): ");
            boolean hadDog = scanner.nextLine().equalsIgnoreCase("y");
            System.out.print("Do you have behavioral knowledge? (y/n): ");
            boolean behaviorKnowledge = scanner.nextLine().equalsIgnoreCase("y");
            System.out.print("Willing to work with behavioral issues? (y/n): ");
            boolean behaviorIssues = scanner.nextLine().equalsIgnoreCase("y");

            AdopterProfile newProfile = new AdopterProfile(
                    energy, garden, cats, dogs, kids,
                    budget, disabled, hadDog,
                    behaviorKnowledge, behaviorIssues
            );

            adopter.setProfile(newProfile);
            System.out.println("\nSuccess: Profile updated. You can now apply for dogs.");
            System.out.println(newProfile);

        } catch (NumberFormatException e) {
            System.out.println("Error: Energy level and budget must be valid numbers.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void handleSubmitApplication(Adopter adopter) {
        if (adopter.getProfile() == null) {
            System.out.println("Error: You must fill the questionnaire (option 3) before applying.");
            return;
        }

        try {
            System.out.print("Enter Dog ID you want to adopt: ");
            int dogId = Integer.parseInt(scanner.nextLine());

            Dog dog = shelter.findDogById(dogId)
                    .orElseThrow(() -> new IllegalArgumentException("Dog with ID " + dogId + " not found."));

            if (dog.isAdopted()) {
                System.out.println("This dog is already adopted.");
                return;
            }

            System.out.print("Additional notes for the shelter: ");
            String notes = scanner.nextLine();

            int appId = adoptionService.getAllApplications().size() + 2000;

            adoptionService.addApplication(appId, dog, adopter, notes);
            System.out.println("Application submitted successfully! Your match score was high enough.");

        } catch (IllegalStateException e) {
            System.out.println("Application Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleShowMyApplications(Adopter adopter) {
        System.out.println("\n--- YOUR APPLICATIONS ---");
        var myApps = adoptionService.findApplicationsByAdopter(adopter);

        if (myApps.isEmpty()) {
            System.out.println("No applications found.");
        } else {
            myApps.forEach(app -> {
                System.out.println("-----------------------");
                System.out.println(app);
            });
        }
    }
}