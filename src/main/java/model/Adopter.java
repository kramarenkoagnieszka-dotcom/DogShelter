package model;

import java.util.ArrayList;
import java.util.List;

public class Adopter extends User {
    private AdopterProfile profile;
    private List<Donation> donationHistory;

    public Adopter(int id, String firstName, String lastName, String username, String password, String email) {
        super(id, firstName, lastName, username, password, email);
        this.donationHistory = new ArrayList<>();
        this.profile = null;
    }

    public Adopter(int id, String firstName, String lastName, String username, String password, String email, AdopterProfile profile) {
        super(id, firstName, lastName, username, password, email);
        this.profile = profile;
        this.donationHistory = new ArrayList<>();
    }
    private Adopter(){}

    public AdopterProfile getProfile() { return profile; }
    public List<Donation> getDonationHistory() { return donationHistory; }

    public void setProfile(AdopterProfile profile) { this.profile = profile; }
}