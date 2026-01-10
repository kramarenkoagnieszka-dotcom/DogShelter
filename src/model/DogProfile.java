package model;

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

    public int getEnergyLevel() { return energyLevel; }
    public boolean isNeedsGarden() { return needsGarden; }
    public boolean isGoodWithCats() { return goodWithCats; }
    public boolean isGoodWithDogs() { return goodWithDogs; }
    public boolean isGoodWithKids() { return goodWithKids; }
    public boolean isSpecialMedicalNeeds() { return specialMedicalNeeds; }
    public boolean isSpecialBehavioralNeeds() { return specialBehavioralNeeds; }
}