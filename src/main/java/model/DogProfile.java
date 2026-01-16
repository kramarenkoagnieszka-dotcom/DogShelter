package main.java.model;

public class DogProfile {
    private final int energyLevel;
    private final boolean needsGarden;
    private final boolean goodWithCats;
    private final boolean goodWithDogs;
    private final boolean goodWithKids;
    private final boolean specialMedicalNeeds;
    private final boolean specialBehavioralNeeds;

    public DogProfile(int energyLevel, boolean needsGarden, boolean goodWithCats,
                      boolean goodWithDogs, boolean goodWithKids,
                      boolean specialMedicalNeeds, boolean specialBehavioralNeeds) {
        this.energyLevel = energyLevel;
        this.needsGarden = needsGarden;
        this.goodWithCats = goodWithCats;
        this.goodWithDogs = goodWithDogs;
        this.goodWithKids = goodWithKids;
        this.specialMedicalNeeds = specialMedicalNeeds;
        this.specialBehavioralNeeds = specialBehavioralNeeds;
    }
    @Override
    public String toString() {
        return String.format(
                "Profile Details:\n" +
                        "  - Energy Level: %d/5\n" +
                        "  - Needs Garden: %s\n" +
                        "  - Good with Cats: %s\n" +
                        "  - Good with Dogs: %s\n" +
                        "  - Good with Kids: %s\n" +
                        "  - Special Medical Needs: %s\n" +
                        "  - Special Behavioral Needs: %s",
                energyLevel,
                needsGarden ? "YES" : "NO",
                goodWithCats ? "YES" : "NO",
                goodWithDogs ? "YES" : "NO",
                goodWithKids ? "YES" : "NO",
                specialMedicalNeeds ? "YES" : "NO",
                specialBehavioralNeeds ? "YES" : "NO"
        );
    }

    public int getEnergyLevel() { return energyLevel; }
    public boolean isNeedsGarden() { return needsGarden; }
    public boolean isGoodWithCats() { return goodWithCats; }
    public boolean isGoodWithDogs() { return goodWithDogs; }
    public boolean isGoodWithKids() { return goodWithKids; }
    public boolean isSpecialMedicalNeeds() { return specialMedicalNeeds; }
    public boolean isSpecialBehavioralNeeds() { return specialBehavioralNeeds; }
}