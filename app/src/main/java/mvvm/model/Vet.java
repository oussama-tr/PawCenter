package mvvm.model;

import androidx.annotation.NonNull;

public class Vet {
    private int id;
    private String firstname;
    private String lastname;
    private String imageUrl;

    public Vet(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @NonNull
    @Override
    public String toString() {
        return "Dr. " + firstname + " " + lastname;
    }

    public Vet(int id, String firstname, String lastname, String imageUrl) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
