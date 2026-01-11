package service;

import model.Dog;
import model.Staff;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Shelter {
    private List<Dog> dogs = new ArrayList<>();
    private List<Staff> employees = new ArrayList<>();

    public void addDog(Dog dog) {
        dogs.add(dog);
    }

    public void removeDogAfterAdoption(int dogId) {
        dogs.removeIf(d -> d.getId() == dogId);
    }

    public Optional <Dog> findDogById (int dogId){
        return dogs.stream()
                .filter(dog -> dog.getId() == dogId)
                .findFirst();
    }

    public void addEmployee(Staff employee) {
        employees.add(employee);
    }

    public void removeEmployee(int employeeId) {
        employees.removeIf(e -> e.getId() == employeeId);
    }

    public List<Dog> getDogs() {
        return new ArrayList<>(dogs);
    }

    public List<Staff> getEmployees() {
        return new ArrayList<>(employees);
    }
}