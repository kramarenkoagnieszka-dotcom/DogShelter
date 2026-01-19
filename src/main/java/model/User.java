package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Staff.class, name = "staff"),
        @JsonSubTypes.Type(value = Donor.class, name = "donor"),
        @JsonSubTypes.Type(value = Adopter.class, name = "adopter"),
        @JsonSubTypes.Type(value = Admin.class, name = "admin")
})

public abstract class User {
    private int id;
    private String username;
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
    protected User(){}

    @Override
    public String toString() {
        return String.format(
                "[%s] ID: %d | Username: %s | Name: %s %s | Email: %s",
                this.getClass().getSimpleName(), id, username, firstName, lastName, email
        );
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