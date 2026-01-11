package model;

import java.time.LocalDateTime;

public class AdoptionApplication {
    private int id;
    private Dog dog;
    private Adopter adopter;
    private LocalDateTime applicationDate;
    private ApplicationStatus status;
    private double matchPercentage;
    private String notes;

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
