package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdoptionApplication {
    private int id;
    private Dog dog;
    private Adopter adopter;
    private LocalDateTime applicationDate;
    private ApplicationStatus status;
    private double matchPercentage;
    private String notes;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AdoptionApplication(int id, Dog dog, Adopter adopter, double matchPercentage, String notes) {
        this.id = id;
        this.dog = dog;
        this.adopter = adopter;
        this.matchPercentage = matchPercentage;
        this.applicationDate = LocalDateTime.now();
        this.status = ApplicationStatus.PENDING;
        this.notes = notes;
    }

    public enum ApplicationStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    @Override
    public String toString() {
        return String.format(
                "Application ID: %d\n" +
                        "Status: %s\n" +
                        "Date: %s\n" +
                        "Match: %.2f%%\n" +
                        "Dog: %s (ID: %d)\n" +
                        "Adopter: %s %s (ID: %d)\n" +
                        "Notes: %s",
                id,
                status,
                applicationDate.format(DATE_FORMATTER),
                matchPercentage,
                dog.getName(), dog.getId(),
                adopter.getFirstName(), adopter.getLastName(), adopter.getId(),
                (notes == null || notes.isEmpty()) ? "None" : notes
        );
    }

    public int getId() { return id; }
    public Dog getDog() { return dog; }
    public Adopter getAdopter() { return adopter; }
    public LocalDateTime getApplicationDate() { return applicationDate; }
    public ApplicationStatus getStatus() { return status; }
    public double getMatchPercentage() { return matchPercentage; }
    public String getNotes() { return notes; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public boolean isProcessed() {
        return this.status != ApplicationStatus.PENDING;
    }
}