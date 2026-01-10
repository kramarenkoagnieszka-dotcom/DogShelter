package model;

public class AdopterProfile {
    private final int energyLevel;
    private final boolean hasGarden;
    private final boolean hasCats;
    private final boolean hasDogs;
    private final boolean hasKids;

    private final double monthlyBudget;
    private final boolean willingForDisabledDog;

    private final boolean hadDogBefore;
    private final boolean hasBehavioralKnowledge;
    private final boolean willingForBehavioralIssues;

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