package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MatchingServiceTest {

    private MatchingService matchingService;

    @BeforeEach
    void setUp() {
        matchingService = new MatchingService();
    }

    @Test
    @DisplayName("Should return 0.0 when critical garden requirement is not met")
    void shouldReturnZeroWhenGardenRequirementFails() {
        DogProfile dogProp = new DogProfile(3, true, true, true, true, false, false);
        AdopterProfile adProp = new AdopterProfile(3, false, false, false, false, 500.0, true, true, true, true);

        Dog dog = new Dog(1, "Rex", 2, "Huskie", dogProp);
        Adopter adopter = new Adopter(1, "John", "Doe", "john", "pass", "john@test.com");
        adopter.setProfile(adProp);

        assertThat(matchingService.calculateMatchPercentage(dog, adopter)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should return 0.0 when adopter is not willing to take a dog with medical needs")
    void shouldReturnZeroWhenMedicalRequirementFails() {
        DogProfile dogProp = new DogProfile(3, false, true, true, true, true, false);
        AdopterProfile adProp = new AdopterProfile(3, true, false, false, false, 500.0, false, true, true, true);

        Dog dog = new Dog(1, "Rex", 2, "Huskie", dogProp);
        Adopter adopter = new Adopter(1, "John", "Doe", "john", "pass", "john@test.com");
        adopter.setProfile(adProp);

        assertThat(matchingService.calculateMatchPercentage(dog, adopter)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Should calculate specific percentage for a high-quality match")
    void shouldCalculateCorrectPercentageForGoodMatch() {
        DogProfile dogProp = new DogProfile(3, false, true, true, true, false, false);
        AdopterProfile adProp = new AdopterProfile(3, true, false, false, true, 500.0, true, true, true, true);

        Dog dog = new Dog(1, "Rex", 2, "Huskie", dogProp);
        Adopter adopter = new Adopter(1, "John", "Doe", "john", "pass", "john@test.com");
        adopter.setProfile(adProp);

        double result = matchingService.calculateMatchPercentage(dog, adopter);

        assertThat(result).isEqualTo(94.44);
    }

    @Test
    @DisplayName("Should score medical budget correctly when special needs are present")
    void shouldScoreMedicalBudget() {
        DogProfile dogProp = new DogProfile(3, false, true, true, true, true, false);

        AdopterProfile lowBudgetProf = new AdopterProfile(3, true, true, true, true, 150.0, true, true, true, true);

        AdopterProfile highBudgetProf = new AdopterProfile(3, true, true, true, true, 500.0, true, true, true, true);

        Dog dog = new Dog(1, "Rex", 2, "Huskie", dogProp);
        Adopter adopter = new Adopter(1, "John", "Doe", "john", "pass", "john@test.com");

        adopter.setProfile(lowBudgetProf);
        double lowScore = matchingService.calculateMatchPercentage(dog, adopter);

        adopter.setProfile(highBudgetProf);
        double highScore = matchingService.calculateMatchPercentage(dog, adopter);

        assertThat(highScore).isGreaterThan(lowScore);
    }

    @Test
    @DisplayName("Should return 100.0 for a perfect theoretical match")
    void shouldReturnFullScoreForPerfectMatch() {
        DogProfile dogProp = new DogProfile(3, true, false, false, true, false, false);
        AdopterProfile adProp = new AdopterProfile(3, true, false, false, true, 500.0, false, false, false, false);

        Dog dog = new Dog(1, "Rex", 2, "Huskie", dogProp);
        Adopter adopter = new Adopter(1, "John", "Doe", "john", "pass", "john@test.com");
        adopter.setProfile(adProp);

        assertThat(matchingService.calculateMatchPercentage(dog, adopter)).isEqualTo(100.0);
    }
}