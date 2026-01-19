package service;

import model.Dog;
import model.DogProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Shelter {
    private List<Dog> dogs;
    private int dogSequence;

    public Shelter() {
        this.dogs = new ArrayList<>();
        this.dogSequence = 0;
    }

    public Shelter(List<Dog> loadedDogs) {
        this.dogs = (loadedDogs != null) ? loadedDogs : new ArrayList<>();
    }

    public void createAndAddDog(String name, String breed, int age, DogProfile profile) {
        int nextId = dogs.stream()
                .mapToInt(Dog::getId)
                .max()
                .orElse(0) + 1;

        Dog newDog = new Dog(nextId, name, age, breed, profile);
        addDog(newDog);
    }

    public void addDog(Dog dog) {
        if (findDogById(dog.getId()).isPresent()) {
            throw new IllegalArgumentException("Dog with ID " + dog.getId() + " already exists in the shelter.");
        }
        dogs.add(dog);
    }

    public void removeDogAfterAdoption(int dogId) {
        boolean removed = dogs.removeIf(d -> d.getId() == dogId);
        if (!removed) {
            throw new IllegalArgumentException("Cannot remove dog: Dog with ID " + dogId + " not found.");
        }
    }

    public Optional<Dog> findDogById(int dogId) {
        return dogs.stream()
                .filter(dog -> dog.getId() == dogId)
                .findFirst();
    }

    public List<Dog> searchDogs(String query) {
        List<Dog> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        try {
            int id = Integer.parseInt(query);
            findDogById(id).ifPresent(results::add);
        } catch (NumberFormatException e) {
        }

        dogs.stream()
                .filter(d -> d.getName().toLowerCase().contains(lowerQuery) ||
                        d.getBreed().toLowerCase().contains(lowerQuery))
                .filter(d -> results.stream().noneMatch(r -> r.getId() == d.getId()))
                .forEach(results::add);

        return results;
    }

    public List<Dog> getDogs() {
        return new ArrayList<>(dogs);
    }
}