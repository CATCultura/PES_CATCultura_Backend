package cat.cultura.backend.entity;

import cat.cultura.backend.factories.GlobalMessageSource;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="username", unique = true)
    private String username;

    @Column(name="nameAndSurname")
    private String nameAndSurname;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;
    @Column(name="creationDate")
    private String creationDate;
    @ElementCollection
    @CollectionTable(name="favourites", joinColumns=@JoinColumn(name="id"))
    @Column(name="favourites")
    private List<Event> favourites = new ArrayList<>();

    public User(String username) {
        this.username = username;
    }

    public User(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNameAndSurname() {
        return nameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Event> getFavourites() {
        return favourites;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setFavourites(List<Event> favourites) {
        this.favourites = favourites;
    }

    public void addFavourite(Event e) {
        if (favourites.contains(e)) throw new AssertionError("Event " + e.getId() + " already in favourites");
        favourites.add(e);
    }

    public void removeFavourite(Event e) {
        if (!favourites.contains(e)) throw new AssertionError("Event " + e.getId() +  " is not in favourites");
        favourites.remove(e);
    }
}
