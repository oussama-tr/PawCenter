package mvvm.model;

import java.util.Date;

public class Notification {
    private int id;
    private Pet pet;
    private User user;
    private String content;
    private Date date;
    private boolean seen;
    private boolean forAdmin;

    public Notification(int id, Pet pet, User user, String content, Date date, boolean seen, boolean forAdmin) {
        this.id = id;
        this.pet = pet;
        this.user = user;
        this.content = content;
        this.date = date;
        this.seen = seen;
        this.forAdmin = forAdmin;
    }

    public Notification(User user, String content, boolean forAdmin) {
        this.user = user;
        this.content = content;
        this.forAdmin = forAdmin;
    }

    public Notification(Pet pet, String content, Date date, boolean seen, boolean forAdmin) {
        this.pet = pet;
        this.content = content;
        this.date = date;
        this.seen = seen;
        this.forAdmin = forAdmin;
    }

    public Notification(int id, User user, String content, Date date, boolean seen, boolean forAdmin) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
        this.seen = seen;
        this.forAdmin = forAdmin;
    }

    public Notification(String content, Date date, boolean seen, boolean forAdmin) {
        this.content = content;
        this.date = date;
        this.seen = seen;
        this.forAdmin = forAdmin;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isForAdmin() {
        return forAdmin;
    }

    public void setForAdmin(boolean forAdmin) {
        this.forAdmin = forAdmin;
    }
}
