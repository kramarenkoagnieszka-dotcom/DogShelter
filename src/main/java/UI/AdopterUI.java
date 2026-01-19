package UI;

import model.Adopter;
import model.AdopterProfile;
import model.Dog;
import service.AdoptionService;
import service.Shelter;
import java.util.List;
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
                case "1" -> handleBrowseDogs(adopter);
                case "2" -> handleSearchDogs();
                case "3" -> handleUpdateQuestionnaire(adopter);
                case "4" -> handleSubmitApplication(adopter);
                case "5" -> handleShowMyApplications(adopter);
                case "6" -> active = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void handleBrowseDogs(Adopter adopter) {
        List<Dog> allDogs = shelter.getDogs();
        if (allDogs.isEmpty()) {
            System.out.println("No dogs available in the shelter.");
            return;
        }

        if (adopter.getProfile() == null) {
            System.out.println("\n--- AVAILABLE DOGS (Standard List) ---");
            System.out.println("Note: Fill your profile to see matches sorted by compatibility.");
            allDogs.forEach(System.out::println);
        } else {
            System.out.println("\n--- RECOMMENDED DOGS FOR YOU ---");
            var rankedDogs = adoptionService.getRankedDogsForAdopter(adopter, allDogs);

            for (AdoptionService.DogMatchResult result : rankedDogs) {
                System.out.printf("%3.0f%% Match\n%s\n\n", result.percentage(), result.dog());
            }
        }
    }


    private void handleUpdateQuestionnaire(Adopter adopter) {
        System.out.println("\n--- ADOPTION QUESTIONNAIRE (DETAILED) ---");
        try {
            System.out.print("Preferred Dog Energy Level (1-5): ");
            int energy = Integer.parseInt(scanner.nextLine());

            boolean garden = askYesNo("Do you have a garden?");
            boolean cats = askYesNo("Do you have cats?");
            boolean dogs = askYesNo("Do you have other dogs?");
            boolean kids = askYesNo("Do you have children?");

            System.out.print("Monthly budget for dog care (PLN): ");
            double budget = Double.parseDouble(scanner.nextLine());

            boolean disabled = askYesNo("Are you willing to adopt a disabled dog?");
            boolean hadDog = askYesNo("Have you had a dog before?");
            boolean behaviorKnowledge = askYesNo("Do you have behavioral knowledge?");
            boolean behaviorIssues = askYesNo("Willing to work with behavioral issues?");

            AdopterProfile newProfile = new AdopterProfile(
                    energy, garden, cats, dogs, kids,
                    budget, disabled, hadDog,
                    behaviorKnowledge, behaviorIssues
            );

            adoptionService.updateAdopterProfile(adopter, newProfile);

            System.out.println("\nSuccess: Profile updated. You can now apply for dogs.");
            System.out.println(newProfile);

        } catch (NumberFormatException e) {
            System.out.println("Error: Energy level and budget must be valid numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid data: " + e.getMessage());
        }
    }

    private void handleSubmitApplication(Adopter adopter) {
        try {
            System.out.print("Enter Dog ID you want to adopt: ");
            int dogId = Integer.parseInt(scanner.nextLine());

            System.out.print("Additional notes for the shelter: ");
            String notes = scanner.nextLine();

            adoptionService.processAdoptionRequest(adopter, dogId, notes);

            System.out.println("Application submitted successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Dog ID must be a number.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Application Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void handleShowMyApplications(Adopter adopter) {
        System.out.println("\n--- YOUR APPLICATIONS ---");
        var myApps = adoptionService.findApplicationsByAdopter(adopter);

        if (myApps.isEmpty()) {
            System.out.println("No applications found.");
        } else {
            myApps.forEach(System.out::println);
        }
    }

    private boolean askYesNo(String message) {
        System.out.print(message + " (y/n): ");
        return scanner.nextLine().equalsIgnoreCase("y");
    }
}