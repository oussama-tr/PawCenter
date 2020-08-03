package mvvm.model;

import java.io.Serializable;

public class Pet implements Serializable {
    int id;
    String name;
    String species;
    String gender;
    String breed;
    int age;
    float weight;
    boolean neutered;
    boolean vaccinated;
    String imageUrl;
    User owner;

    public Pet(int id, String name, String species, String gender, String breed, int age, float weight, boolean neutered, boolean vaccinated, String imageUrl, User owner) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.neutered = neutered;
        this.vaccinated = vaccinated;
        this.imageUrl = imageUrl;
        this.owner = owner;
    }

    public Pet(int id, String name, String species, String gender, String breed, int age, float weight, boolean neutered, boolean vaccinated, String imageUrl) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.neutered = neutered;
        this.vaccinated = vaccinated;
        this.imageUrl = imageUrl;
    }

    public Pet(String name, String species, String gender, String breed, int age, float weight, boolean neutered, boolean vaccinated, User owner) {
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.neutered = neutered;
        this.vaccinated = vaccinated;
        this.owner = owner;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
