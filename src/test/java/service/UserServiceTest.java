package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Nested
    @DisplayName("Method: createAndAddUser")
    class CreateAndAddUserTests {
        @Test
        void shouldCreateCorrectUserRole() {
            userService.createAndAddUser("1", "John", "Doe", "john_staff", "pass", "john@test.com");
            userService.createAndAddUser("2", "Jane", "Smith", "jane_adopter", "pass", "jane@test.com");

            List<User> users = userService.getAllUsers();
            assertThat(users).hasSize(2);
            assertThat(users.get(0)).isInstanceOf(Staff.class);
            assertThat(users.get(1)).isInstanceOf(Adopter.class);
        }

        @Test
        void shouldThrowExceptionForInvalidRole() {
            assertThatThrownBy(() ->
                    userService.createAndAddUser("99", "Name", "Last", "user", "pass", "mail@test.com"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid role selected");
        }

        @Test
        void shouldStartIdsFrom101() {
            userService.createAndAddUser("1", "John", "Doe", "john", "pass", "john@test.com");

            assertThat(userService.getAllUsers().get(0).getId()).isEqualTo(101);
        }
    }

    @Nested
    @DisplayName("Method: addUser")
    class AddUserTests {
        @Test
        void shouldThrowExceptionWhenUsernameIsTaken() {
            User staff = new Staff(1, "John", "Doe", "unique_user", "pass", "mail1@test.com");
            User adopter = new Adopter(2, "Jane", "Smith", "UNIQUE_USER", "pass", "mail2@test.com");

            userService.addUser(staff);

            assertThatThrownBy(() -> userService.addUser(adopter))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("already taken");
        }

        @Test
        void shouldAllowOnlyOneAdmin() {
            User admin1 = new Admin(1, "Admin1", "One", "admin1", "pass", "a1@test.com");
            User admin2 = new Admin(2, "Admin2", "Two", "admin2", "pass", "a2@test.com");

            userService.addUser(admin1);

            assertThatThrownBy(() -> userService.addUser(admin2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("only have one administrator");
        }
    }

    @Nested
    @DisplayName("Method: login")
    class LoginTests {
        @Test
        void shouldReturnUserOnSuccessfulLogin() {
            userService.createAndAddUser("4", "Admin", "User", "admin", "secret", "admin@test.com");

            User loggedIn = userService.login("admin", "secret");

            assertThat(loggedIn).isNotNull();
            assertThat(loggedIn.getUsername()).isEqualTo("admin");
        }

        @Test
        void shouldThrowExceptionOnInvalidCredentials() {
            userService.createAndAddUser("1", "User", "One", "user1", "pass1", "u1@test.com");

            assertThatThrownBy(() -> userService.login("user1", "wrong_pass"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid username or password");
        }
    }

    @Nested
    @DisplayName("Method: removeUserById")
    class RemoveUserTests {
        @Test
        void shouldRemoveNonAdminUser() {
            userService.createAndAddUser("2", "Adopter", "User", "adopter", "pass", "a@test.com");
            int id = userService.getAllUsers().get(0).getId();

            userService.removeUserById(id);

            assertThat(userService.getAllUsers()).isEmpty();
        }

        @Test
        void shouldNotAllowAdminDeletion() {
            userService.createAndAddUser("4", "Admin", "User", "admin", "pass", "admin@test.com");
            int adminId = userService.getAllUsers().get(0).getId();

            assertThatThrownBy(() -> userService.removeUserById(adminId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("administrator account cannot be deleted");
        }
    }
}