package service;

import model.User;
import model.Admin;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private List<User> users = new ArrayList<>();

    public void addUser(User newUser) {
        if (newUser instanceof Admin && hasAdmin()) {
            throw new IllegalStateException("The system can only have one administrator.");
        }
        users.add(newUser);
    }

    private boolean hasAdmin() {
        return users.stream().anyMatch(u -> u instanceof Admin);
    }

    public void removeUserById(int id) {
        User userToRemove = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found."));

        if (userToRemove instanceof Admin) {
            throw new IllegalStateException("The administrator account cannot be deleted.");
        }

        users.remove(userToRemove);
    }

    public Optional<User> login(String login, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(login) && u.getPassword().equals(password))
                .findFirst();
    }

    public Optional<User> findUserById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}