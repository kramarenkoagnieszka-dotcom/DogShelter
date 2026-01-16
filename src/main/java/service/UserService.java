package service;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private List<User> users = new ArrayList<>();

    public void createAndAddUser(String roleChoice, String firstName, String lastName,
                                 String username, String password, String email) {

        int nextId = users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(100) + 1;

        User newUser = switch (roleChoice) {
            case "1" -> new Staff(nextId, firstName, lastName, username, password, email);
            case "2" -> new Adopter(nextId, firstName, lastName, username, password, email);
            case "3" -> new Donor(nextId, firstName, lastName, username, password, email);
            case "4" -> new Admin(nextId, firstName, lastName, username, password, email);
            default -> throw new IllegalArgumentException("Invalid role selected: " + roleChoice);
        };

        addUser(newUser);
    }

    public void addUser(User newUser) {
        if (users.stream().anyMatch(u -> u.getId() == newUser.getId())) {
            throw new IllegalArgumentException("User with ID " + newUser.getId() + " already exists.");
        }
        if (users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(newUser.getUsername()))) {
            throw new IllegalArgumentException("Username '" + newUser.getUsername() + "' is already taken.");
        }
        if (newUser instanceof Admin && hasAdmin()) {
            throw new IllegalStateException("The system can only have one administrator.");
        }

        users.add(newUser);
    }

    public User login(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
    }

    public void removeUserById(int id) {
        User userToRemove = findUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found."));

        if (userToRemove instanceof Admin) {
            throw new IllegalStateException("The administrator account cannot be deleted.");
        }

        users.remove(userToRemove);
    }

    public Optional<User> findUserById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    private boolean hasAdmin() {
        return users.stream().anyMatch(u -> u instanceof Admin);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}