package service;

import model.Dog;
import model.DogProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ShelterTest {

    private Shelter shelter;
    private DogProfile defaultProfile;

    @BeforeEach
    void setUp() {
        shelter = new Shelter();
        defaultProfile = new DogProfile(3, false, true, true, true, false, false);
    }

    @Nested
    @DisplayName("Method: createAndAddDog")
    class CreateAndAddDogTests {
        @Test
        void shouldIncrementIdCorrectly() {
            shelter.createAndAddDog("Burek", "Mixed", 3, defaultProfile);
            shelter.createAndAddDog("Reksio", "Terrier", 5, defaultProfile);

            List<Dog> dogs = shelter.getDogs();
            assertThat(dogs).hasSize(2);
            assertThat(dogs.get(0).getId()).isEqualTo(1);
            assertThat(dogs.get(1).getId()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Method: addDog")
    class AddDogTests {
        @Test
        void shouldThrowExceptionWhenIdAlreadyExists() {
            Dog dog1 = new Dog(10, "Ares", 2, "Boxer", defaultProfile);
            Dog dog2 = new Dog(10, "Azor", 4, "Mops", defaultProfile);

            shelter.addDog(dog1);

            assertThatThrownBy(() -> shelter.addDog(dog2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("already exists");
        }
    }

    @Nested
    @DisplayName("Method: removeDogAfterAdoption")
    class RemoveDogTests {
        @Test
        void shouldRemoveDogWhenIdExists() {
            shelter.createAndAddDog("Pikus", "Labrador", 1, defaultProfile);
            int dogId = shelter.getDogs().get(0).getId();

            shelter.removeDogAfterAdoption(dogId);

            assertThat(shelter.getDogs()).isEmpty();
        }

        @Test
        void shouldThrowExceptionWhenDogNotFound() {
            assertThatThrownBy(() -> shelter.removeDogAfterAdoption(999))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not found");
        }
    }

    @Nested
    @DisplayName("Method: searchDogs")
    class SearchDogsTests {
        @BeforeEach
        void fillShelter() {
            shelter.addDog(new Dog(1, "Burek", 5, "Shepherd", defaultProfile));
            shelter.addDog(new Dog(2, "Azor", 3, "Beagle", defaultProfile));
            shelter.addDog(new Dog(3, "Luna", 2, "Shepherd", defaultProfile));
        }

        @Test
        void shouldFindDogBySpecificIdAsString() {
            List<Dog> results = shelter.searchDogs("2");
            assertThat(results).hasSize(1);
            assertThat(results.get(0).getName()).isEqualTo("Azor");
        }

        @Test
        void shouldFindDogsByBreedFragmentCaseInsensitive() {
            List<Dog> results = shelter.searchDogs("SHEPHERD");
            assertThat(results).hasSize(2);
            assertThat(results).extracting(Dog::getName).containsExactlyInAnyOrder("Burek", "Luna");
        }

        @Test
        void shouldFindDogsByNameFragment() {
            List<Dog> results = shelter.searchDogs("ur");
            assertThat(results).hasSize(1);
            assertThat(results.get(0).getName()).isEqualTo("Burek");
        }

        @Test
        void shouldNotReturnDuplicates() {
            List<Dog> results = shelter.searchDogs("1");
            assertThat(results).hasSize(1);
        }
    }
}