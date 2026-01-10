package model;

public class Dog {
    private final int id;
    private String name;
    private int age;
    private String breed;
    private boolean isAdopted;
    private DogProfile profile;

    public Dog(int id, String name, int age, String breed, DogProfile profile) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.profile = profile;
        this.isAdopted = false;
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

    @Override
    public String toString() {
        return "Dog{id=" + id + ", name='" + name + "', breed='" + breed + "', adopted=" + isAdopted + "}";
    }
}