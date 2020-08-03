package mvvm.model;

import java.io.Serializable;

public class Appointment implements Serializable {
    private int id;
    private Pet pet;
    private Vet vet;
    private String date;
    private String reason;
    private boolean confirmed;
    private User user;

    public Appointment(int id, Pet pet, Vet vet, String date, String reason, boolean confirmed, User user) {
        this.id = id;
        this.pet = pet;
        this.vet = vet;
        this.date = date;
        this.reason = reason;
        this.confirmed = confirmed;
        this.user = user;
    }

    public Appointment(Pet pet, Vet vet, String date, String reason) {
        this.pet = pet;
        this.vet = vet;
        this.date = date;
        this.reason = reason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Vet getVet() {
        return vet;
    }

    public void setVet(Vet vet) {
        this.vet = vet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
