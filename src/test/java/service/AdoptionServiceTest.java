package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

class AdoptionServiceTest {

    private AdoptionService adoptionService;
    private MatchingService matchingService;
    private Shelter shelter;
    private Adopter adopter;
    private Dog dog;

    @BeforeEach
    void setUp() {
        matchingService = new MatchingService();
        shelter = new Shelter();
        adoptionService = new AdoptionService(matchingService, shelter);

        adopter = new Adopter(1, "John", "Doe", "john_d", "pass", "john@test.com");

        DogProfile dogProfile = new DogProfile(3, false, true, true, true, false, false);
        shelter.createAndAddDog("Rex", "Labrador", 3, dogProfile);
        dog = shelter.getDogs().get(0);
    }

    @Nested
    @DisplayName("Method: processAdoptionRequest")
    class ProcessRequestTests {
        @Test
        void shouldThrowExceptionWhenProfileIsMissing() {
            assertThatThrownBy(() -> adoptionService.processAdoptionRequest(adopter, dog.getId(), "Notes"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("fill the questionnaire");
        }

        @Test
        void shouldRejectApplicationWhenMatchPercentageIsTooLow() {
            // Dog needs garden, adopter has none -> Match 0.0%
            DogProfile dogNeedsGarden = new DogProfile(3, true, true, true, true, false, false);
            shelter.createAndAddDog("GardenDog", "Husky", 2, dogNeedsGarden);
            int gardenDogId = shelter.getDogs().get(1).getId();

            AdopterProfile noGardenProfile = new AdopterProfile(3, false, false, false, false, 100.0, false, false, false, false);
            adopter.setProfile(noGardenProfile);

            assertThatThrownBy(() -> adoptionService.processAdoptionRequest(adopter, gardenDogId, "Notes"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Match percentage is too low");
        }

        @Test
        void shouldCreateApplicationWhenCriteriaAreMet() {
            AdopterProfile perfectProfile = new AdopterProfile(3, false, false, false, false, 500.0, true, true, true, true);
            adopter.setProfile(perfectProfile);

            adoptionService.processAdoptionRequest(adopter, dog.getId(), "I love dogs");

            assertThat(adoptionService.getAllApplications()).hasSize(1);
            assertThat(adoptionService.getAllApplications().get(0).getNotes()).isEqualTo("I love dogs");
        }
    }

    @Nested
    @DisplayName("Method: changeStatus")
    class ChangeStatusTests {
        private AdoptionApplication application;

        @BeforeEach
        void createPendingApplication() {
            AdopterProfile profile = new AdopterProfile(3, false, false, false, false, 500.0, true, true, true, true);
            adopter.setProfile(profile);
            adoptionService.processAdoptionRequest(adopter, dog.getId(), "Notes");
            application = adoptionService.getAllApplications().get(0);
        }

        @Test
        void shouldRemoveDogFromShelterWhenAccepted() {
            adoptionService.changeStatus(application.getId(), AdoptionApplication.ApplicationStatus.ACCEPTED);

            assertThat(shelter.getDogs()).isEmpty();
            assertThat(application.getStatus()).isEqualTo(AdoptionApplication.ApplicationStatus.ACCEPTED);
        }

        @Test
        void shouldThrowExceptionWhenChangingStatusOfAlreadyProcessedApplication() {
            adoptionService.changeStatus(application.getId(), AdoptionApplication.ApplicationStatus.REJECTED);

            assertThatThrownBy(() -> adoptionService.changeStatus(application.getId(), AdoptionApplication.ApplicationStatus.ACCEPTED))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("already processed");
        }
    }

    @Nested
    @DisplayName("Method: cleanOldApplications")
    class CleanupTests {
        @Test
        void shouldNotRemoveRecentApplications() {
            AdopterProfile profile = new AdopterProfile(3, false, false, false, false, 500.0, true, true, true, true);
            adopter.setProfile(profile);

            adoptionService.processAdoptionRequest(adopter, dog.getId(), "Notes");
            AdoptionApplication app = adoptionService.getAllApplications().get(0);
            adoptionService.changeStatus(app.getId(), AdoptionApplication.ApplicationStatus.REJECTED);

            adoptionService.cleanOldApplications();

            assertThat(adoptionService.getAllApplications()).hasSize(1);
        }
    }
}