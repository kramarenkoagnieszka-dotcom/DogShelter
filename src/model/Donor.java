package model;

import java.util.ArrayList;
import java.util.List;

public class Donor extends User {
    private List<Donation> donationHistory;
    public Donor (int id, String firstName, String lastName, String username, String password, String email) {
        super(id, firstName, lastName, username, password, email);
        this.donationHistory = new ArrayList<>();
    }
    public void addDonation(Donation donation) {
        this.donationHistory.add(donation);
    }

    public List<Donation> getDonationHistory() {
        return donationHistory;
    }
}
