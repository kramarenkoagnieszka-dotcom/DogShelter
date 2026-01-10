package model;

public abstract class User {
    private final int id;
    private final String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    public User(int id, String firstName, String lastName, String username, String password, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}