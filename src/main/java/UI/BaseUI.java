package UI;

import service.Shelter;
import model.Dog;
import java.util.Scanner;
import java.util.List;

public abstract class BaseUI {
    protected final Scanner scanner;
    protected final Shelter shelter;

    public BaseUI(Scanner scanner, Shelter shelter) {
        this.scanner = scanner;
        this.shelter = shelter;
    }

    protected void handleBrowseDogs() {
        List<Dog> dogs = shelter.getDogs();
        if (dogs.isEmpty()) {
            System.out.println("The shelter is currently empty.");
        } else {
            System.out.println("\n--- ALL DOGS ---");
            dogs.forEach(dog -> {
                System.out.println(dog);
                System.out.println("-----------------------");
            });
        }
    }

    protected void handleSearchDogs() {
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
}