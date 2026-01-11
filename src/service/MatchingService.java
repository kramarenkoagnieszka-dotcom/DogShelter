package service;

import model.AdopterProfile;
import model.Dog;
import model.Adopter;
import model.DogProfile;

public class MatchingService {


    public double calculateMatchPercentage(Dog dog, Adopter adopter) {
        if (dog.getProfile() == null || adopter.getProfile() == null) {
            return 0.0;
        }

        if (!satisfiesCriticalRequirements(dog, adopter)) {
            return 0.0;
        }

        int totalScore = 0;
        totalScore += scoreGarden(dog, adopter);
        totalScore += scorePets(dog, adopter);
        totalScore += scoreChildren(dog, adopter);
        totalScore += scoreMedical(dog, adopter);
        totalScore += scoreBehavioral(dog, adopter);

        double finalPercentage = (double) totalScore / 15 * 100;

        return Math.round(finalPercentage * 100.0) / 100.0;
    }

    private boolean satisfiesCriticalRequirements(Dog dog, Adopter adopter) {
        DogProfile dogProfile = dog.getProfile();
        AdopterProfile adopterProfile = adopter.getProfile();

        if (dogProfile.isNeedsGarden() && !adopterProfile.isHasGarden()) {
            return false;
        }
        if (!dogProfile.isGoodWithDogs() && adopterProfile.isHasDogs()) {
            return false;
        }
        if (!dogProfile.isGoodWithCats() && adopterProfile.isHasCats()) {
            return false;
        }
        if (dogProfile.isSpecialMedicalNeeds() && !adopterProfile.isWillingForDisabledDog()) {
            return false;
        }
        if (dogProfile.isSpecialBehavioralNeeds() && !adopterProfile.isWillingForBehavioralIssues()) {
            return false;
        }
        return true;
    }

    private int scoreGarden(Dog dog, Adopter adopter) {
        if (adopter.getProfile().isHasGarden()) {return 3; }
        else return 2;

    }

    private int scorePets(Dog dog, Adopter adopter) {
        DogProfile dp = dog.getProfile();
        AdopterProfile ap = adopter.getProfile();

        if ((dp.isGoodWithDogs() == ap.isHasDogs()) && (dp.isGoodWithCats() == ap.isHasCats())) {
            return 3;
        }
        else return 2;
        }

    private int scoreChildren(Dog dog, Adopter adopter) {
        if (dog.getProfile().isGoodWithKids() && adopter.getProfile().isHasKids()) {
            return 3;
        }
        return 2;
    }

    private int scoreMedical(Dog dog, Adopter adopter) {
        DogProfile dp = dog.getProfile();
        double budget = adopter.getProfile().getMonthlyBudget();

        if (dp.isSpecialMedicalNeeds()) {
            if (budget > 300) return 3;
            if (budget >= 200) return 2;
            return 1;
        }
        else return 3;
    }

    private int scoreBehavioral(Dog dog, Adopter adopter) {
        DogProfile dp = dog.getProfile();
        AdopterProfile ap = adopter.getProfile();

        if (dp.isSpecialBehavioralNeeds()) {
            if (ap.isHasBehavioralKnowledge() && ap.isHadDogBefore()) {
                return 3;
            } else if (ap.isHadDogBefore()) {
                return 2;
            } else if (ap.isHasBehavioralKnowledge()) {
                return 1;
            } else {
                return 0;
            }
        }
        return 3;
    }
}