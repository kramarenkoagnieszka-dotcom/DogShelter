package service;

import model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdoptionService {
    private List<AdoptionApplication> applications;
    private final MatchingService matchingService;
    private final Shelter shelter;

    public AdoptionService(MatchingService matchingService, Shelter shelter) {
        this.matchingService = matchingService;
        this.shelter = shelter;
        this.applications = new ArrayList<>();
    }

    public AdoptionService(MatchingService matchingService, Shelter shelter, List<AdoptionApplication> loadedApplications) {
        this.matchingService = matchingService;
        this.shelter = shelter;
        this.applications = (loadedApplications != null) ? loadedApplications : new ArrayList<>();
    }

    public void processAdoptionRequest(Adopter adopter, int dogId, String notes) {
        if (adopter.getProfile() == null) {
            throw new IllegalStateException("You must fill the questionnaire before applying.");
        }

        Dog dog = shelter.findDogById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("Dog with ID " + dogId + " not found."));

        if (dog.isAdopted()) {
            throw new IllegalStateException("Dog " + dog.getName() + " is already adopted.");
        }

        double match = matchingService.calculateMatchPercentage(dog, adopter);

        if (match < 50.0) {
            throw new IllegalStateException("Application rejected: Match percentage is too low ("
                    + String.format("%.1f", match) + "%).");
        }

        int nextId = applications.size() + 2000;

        AdoptionApplication app = new AdoptionApplication(nextId, dog, adopter, match, notes);
        applications.add(app);
    }

    public void updateAdopterProfile(Adopter adopter, AdopterProfile profile) {
        if (adopter == null || profile == null) {
            throw new IllegalArgumentException("Adopter and profile cannot be null.");
        }
        adopter.setProfile(profile);
    }

    public void changeStatus(long id, AdoptionApplication.ApplicationStatus newStatus) {
        AdoptionApplication application = applications.stream()
                .filter(app -> app.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Application not found."));

        if (application.getStatus() != AdoptionApplication.ApplicationStatus.PENDING) {
            throw new IllegalStateException("Application already processed.");
        }

        application.setStatus(newStatus);

        if (newStatus == AdoptionApplication.ApplicationStatus.ACCEPTED) {
            int dogId = application.getDog().getId();
            shelter.removeDogAfterAdoption(dogId);
        }
    }

    public Optional<AdoptionApplication> getApplicationById(long id) {
        cleanOldApplications();
        return applications.stream()
                .filter(app -> app.getId() == id)
                .findFirst();
    }

    public List<AdoptionApplication> findApplicationsByAdopter(Adopter adopter) {
        cleanOldApplications();
        return applications.stream()
                .filter(app -> app.getAdopter().equals(adopter))
                .collect(Collectors.toList());
    }

    public List<AdoptionApplication> getAllApplications() {
        cleanOldApplications();
        return new ArrayList<>(applications);
    }

    public void cleanOldApplications() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(10);

        applications.removeIf(app ->
                (app.getStatus() == AdoptionApplication.ApplicationStatus.ACCEPTED ||
                        app.getStatus() == AdoptionApplication.ApplicationStatus.REJECTED) &&
                        app.getApplicationDate().isBefore(threshold)
        );
    }

    public List<AdoptionApplication> getPendingApplications() {
        return applications.stream()
                .filter(app -> app.getStatus() == AdoptionApplication.ApplicationStatus.PENDING)
                .collect(Collectors.toList());
    }

    public List<AdoptionApplication> getApplications() {
        return new ArrayList<>(applications);
    }

    public record DogMatchResult(Dog dog, double percentage) {}

    public List<DogMatchResult> getRankedDogsForAdopter(Adopter adopter, List<Dog> allDogs) {
        if (adopter.getProfile() == null) {
            return allDogs.stream()
                    .map(dog -> new DogMatchResult(dog, 0.0))
                    .toList();
        }

        return allDogs.stream()
                .map(dog -> {
                    double score = matchingService.calculateMatchPercentage(dog, adopter);
                    return new DogMatchResult(dog, score);
                })
                .sorted((a, b) -> Double.compare(b.percentage(), a.percentage()))
                .toList();
    }
}