package service;

import model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FinancialServiceTest {

    private FinancialService financialService;
    private Shelter shelter;
    private UserService userService;
    private Donor donor;
    private Staff staff;

    @BeforeEach
    void setUp() {
        shelter = new Shelter();
        userService = new UserService();
        financialService = new FinancialService(shelter, userService);

        userService.createAndAddUser("3", "Alice", "Donor", "alice_donor", "pass", "alice@test.com");
        donor = (Donor) userService.getAllUsers().get(0);

        userService.createAndAddUser("1", "Bob", "Staff", "bob_staff", "pass", "bob@test.com");
        staff = (Staff) userService.getAllUsers().get(1);

        DogProfile profile = new DogProfile(3, false, true, true, true, false, false);
        shelter.createAndAddDog("Reksio", "Mixed", 2, profile);
    }

    @Test
    @DisplayName("Should correctly update balance and dog expenses after registration")
    void shouldHandleExpenseRegistrationFlow() {
        financialService.registerDonation(donor, 1000.0);
        Dog dog = shelter.getDogs().get(0);

        financialService.registerExpense(staff, dog.getId(), 250.0, "Medical checkup");

        Assertions.assertThat(financialService.getBalance()).isEqualTo(750.0);

        Assertions.assertThat(dog.getExpenses()).hasSize(1);
        Assertions.assertThat(dog.getExpenses().get(0).getAmount()).isEqualTo(250.0);
        Assertions.assertThat(dog.getExpenses().get(0).getDescription()).isEqualTo("Medical checkup");
    }

    @Test
    @DisplayName("Should throw exception when registering expense for non-existent dog")
    void shouldThrowExceptionForInvalidDogId() {
        financialService.registerDonation(donor, 100.0);

        Assertions.assertThatThrownBy(() -> financialService.registerExpense(staff, 999, 50.0, "Treats"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dog with ID 999 not found");
    }

    @Test
    @DisplayName("Should sum up donations for a specific donor correctly")
    void shouldCalculateTotalDonations() {
        financialService.registerDonation(donor, 100.0);
        financialService.registerDonation(donor, 250.0);

        double total = financialService.calculateTotalDonatedBy(donor);

        Assertions.assertThat(total).isEqualTo(350.0);
    }
}