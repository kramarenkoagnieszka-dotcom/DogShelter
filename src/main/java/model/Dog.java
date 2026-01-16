package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Dog {
    private final int id;
    private String name;
    private int age;
    private String breed;
    private boolean isAdopted;
    private DogProfile profile;
    private List<Expense> expenseList;

    public Dog(int id, String name, int age, String breed, DogProfile profile) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.profile = profile;
        this.isAdopted = false;
        expenseList = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public boolean isAdopted() { return isAdopted; }
    public void setAdopted(boolean adopted) { isAdopted = adopted; }
    public DogProfile getProfile() { return profile; }
    public void setProfile(DogProfile profile) { this.profile = profile; }
    public List<Expense> getExpenses() { return expenseList; }

    public void addExpense(Expense expense) { this.expenseList.add(expense);}

    @Override
    public String toString() {
        return String.format(
                "Dog ID: %d\n" +
                        "Name: %s\n" +
                        "Breed: %s\n" +
                        "Age: %d\n" +
                        "Status: %s\n" +
                        "%s",
                id,
                name,
                breed,
                age,
                isAdopted ? "Adopted" : "Available",
                profile != null ? profile.toString() : "No profile assigned."
        );
    }
}