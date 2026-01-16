package main.java.service;

import main.java.model.AdopterProfile;
import main.java.model.Dog;
import main.java.model.Adopter;
import main.java.model.DogProfile;

public class MatchingService {

    public double calculateMatchPercentage(Dog dog, Adopter adopter) {
        if (dog.getProfile() == null || adopter.getProfile() == null) {
            return 0.0;
        }

        if (!satisfiesCriticalRequirements(dog, adopter)) {
            return 0.0;
        }

        int totalScore = 0;
        totalScore += scoreGarden(adopter);
        totalScore += scorePets(dog, adopter);
        totalScore += scoreChildren(dog, adopter);
        totalScore += scoreMedical(dog, adopter);
        totalScore += scoreBehavioral(dog, adopter);
        totalScore += scoreEnergyLevel(dog, adopter);

        double finalPercentage = (double) totalScore / 18 * 100;

        return Math.round(finalPercentage * 100.0) / 100.0;
    }

    private int scoreEnergyLevel(Dog dog, Adopter adopter) {
        int dogEnergy = dog.getProfile().getEnergyLevel();
        int adopterActivity = adopter.getProfile().getEnergyLevel();

        int difference = Math.abs(dogEnergy - adopterActivity);

        if (difference == 0) return 3;
        if (difference == 1) return 2;
        if (difference == 2) return 1;
        return 0;
    }

    private boolean satisfiesCriticalRequirements(Dog dog, Adopter adopter) {
        DogProfile dogProfile = dog.getProfile();
        AdopterProfile adopterProfile = adopter.getProfile();

        if (dogProfile.isNeedsGarden() && !adopterProfile.isHasGarden()) return false;
        if (!dogProfile.isGoodWithDogs() && adopterProfile.isHasDogs()) return false;
        if (!dogProfile.isGoodWithCats() && adopterProfile.isHasCats()) return false;
        if (dogProfile.isSpecialMedicalNeeds() && !adopterProfile.isWillingForDisabledDog()) return false;
        if (dogProfile.isSpecialBehavioralNeeds() && !adopterProfile.isWillingForBehavioralIssues()) return false;
        return true;
    }

    private int scoreGarden(Adopter adopter) {
        return adopter.getProfile().isHasGarden() ? 3 : 2;
    }

    private int scorePets(Dog dog, Adopter adopter) {
        DogProfile dp = dog.getProfile();
        AdopterProfile ap = adopter.getProfile();
        return (dp.isGoodWithDogs() == ap.isHasDogs() && dp.isGoodWithCats() == ap.isHasCats()) ? 3 : 2;
    }

    private int scoreChildren(Dog dog, Adopter adopter) {
        return (dog.getProfile().isGoodWithKids() && adopter.getProfile().isHasKids()) ? 3 : 2;
    }

    private int scoreMedical(Dog dog, Adopter adopter) {
        DogProfile dp = dog.getProfile();
        double budget = adopter.getProfile().getMonthlyBudget();
        if (dp.isSpecialMedicalNeeds()) {
            if (budget > 300) return 3;
            if (budget >= 200) return 2;
            return 1;
        }
        return 3;
    }

    private int scoreBehavioral(Dog dog, Adopter adopter) {
        DogProfile dp = dog.getProfile();
        AdopterProfile ap = adopter.getProfile();
        if (dp.isSpecialBehavioralNeeds()) {
            if (ap.isHasBehavioralKnowledge() && ap.isHadDogBefore()) return 3;
            if (ap.isHadDogBefore()) return 2;
            if (ap.isHasBehavioralKnowledge()) return 1;
            return 0;
        }
        return 3;
    }
}