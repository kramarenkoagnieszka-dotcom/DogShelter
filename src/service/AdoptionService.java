package service;

import model.AdoptionApplication;
import model.Dog;
import model.Adopter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdoptionService {
    private List<AdoptionApplication> applications = new ArrayList<>();
    private final MatchingService matchingService;

    public AdoptionService(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    public void addApplication(int id, Dog dog, Adopter adopter, String notes) {
        double match = matchingService.calculateMatchPercentage(dog, adopter);

        if (match < 50.0) {
            throw new IllegalStateException("Application rejected: Match percentage is too low (" + match + "%) or critical requirements not met.");
        }

        if (applications.stream().anyMatch(app -> app.getId() == id)) {
            throw new IllegalArgumentException("Application with ID " + id + " already exists.");
        }

        AdoptionApplication app = new AdoptionApplication(id, dog, adopter, match, notes);
        applications.add(app);
    }

    public void changeStatus(long id, AdoptionApplication.ApplicationStatus newStatus) {
        AdoptionApplication application = applications.stream()
                .filter(app -> app.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Application with ID " + id + " not found."));

        application.setStatus(newStatus);
    }

    public Optional<AdoptionApplication> findApplicationById(long id) {
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