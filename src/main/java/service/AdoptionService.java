package service;

import model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdoptionService {
    private List<AdoptionApplication> applications = new ArrayList<>();
    private final MatchingService matchingService;
    private final Shelter shelter;

    public AdoptionService(MatchingService matchingService, Shelter shelter) {
        this.matchingService = matchingService;
        this.shelter = shelter;
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
}