package model;

public class AdopterProfile {
    private int energyLevel;
    private boolean hasGarden;
    private boolean hasCats;
    private boolean hasDogs;
    private boolean hasKids;

    private double monthlyBudget;
    private boolean willingForDisabledDog;

    private boolean hadDogBefore;
    private boolean hasBehavioralKnowledge;
    private boolean willingForBehavioralIssues;

    public AdopterProfile(int energyLevel, boolean hasGarden, boolean hasCats,
                          boolean hasDogs, boolean hasKids, double monthlyBudget,
                          boolean willingForDisabledDog, boolean hadDogBefore,
                          boolean hasBehavioralKnowledge, boolean willingForBehavioralIssues) {
        this.energyLevel = energyLevel;
        this.hasGarden = hasGarden;
        this.hasCats = hasCats;
        this.hasDogs = hasDogs;
        this.hasKids = hasKids;
        this.monthlyBudget = monthlyBudget;
        this.willingForDisabledDog = willingForDisabledDog;
        this.hadDogBefore = hadDogBefore;
        this.hasBehavioralKnowledge = hasBehavioralKnowledge;
        this.willingForBehavioralIssues = willingForBehavioralIssues;
    }
    private AdopterProfile(){}
    @Override
    public String toString() {
        return String.format(
                "Adopter Profile Details:\n" +
                        "  - Activity Level: %d/5\n" +
                        "  - Has Garden: %s\n" +
                        "  - Has Cats: %s\n" +
                        "  - Has Dogs: %s\n" +
                        "  - Has Kids: %s\n" +
                        "  - Monthly Budget: %.2f\n" +
                        "  - Willing for Disabled Dog: %s\n" +
                        "  - Had Dog Before: %s\n" +
                        "  - Behavioral Knowledge: %s\n" +
                        "  - Willing for Behavioral Issues: %s",
                energyLevel,
                hasGarden ? "YES" : "NO",
                hasCats ? "YES" : "NO",
                hasDogs ? "YES" : "NO",
                hasKids ? "YES" : "NO",
                monthlyBudget,
                willingForDisabledDog ? "YES" : "NO",
                hadDogBefore ? "YES" : "NO",
                hasBehavioralKnowledge ? "YES" : "NO",
                willingForBehavioralIssues ? "YES" : "NO"
        );
    }

    public int getEnergyLevel() { return energyLevel; }
    public boolean isHasGarden() { return hasGarden; }
    public boolean isHasCats() { return hasCats; }
    public boolean isHasDogs() { return hasDogs; }
    public boolean isHasKids() { return hasKids; }
    public double getMonthlyBudget() { return monthlyBudget; }
    public boolean isWillingForDisabledDog() { return willingForDisabledDog; }
    public boolean isHadDogBefore() { return hadDogBefore; }
    public boolean isHasBehavioralKnowledge() { return hasBehavioralKnowledge; }
    public boolean isWillingForBehavioralIssues() { return willingForBehavioralIssues; }
}