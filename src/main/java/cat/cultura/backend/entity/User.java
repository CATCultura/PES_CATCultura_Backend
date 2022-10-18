package cat.cultura.backend.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
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

    @Column(name="points")
    private int points = 0;
    @ElementCollection
    @CollectionTable(name="favourites", joinColumns=@JoinColumn(name="id"))
    private List<Event> favourites = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="trophies", joinColumns=@JoinColumn(name="id"))
    private List<Trophy> trophies = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="friends", joinColumns=@JoinColumn(name="id"))
    private List<User> friends = new ArrayList<>();

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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Event> getFavourites() {
        return favourites;
    }


    public void setFavourites(List<Event> favourites) {
        this.favourites = favourites;
    }

    public List<Trophy> getTrophies() {
        return trophies;
    }

    public void setTrophies(List<Trophy> trophies) {
        this.trophies = trophies;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void addFavourite(Event e) {
        if (favourites.contains(e)) throw new AssertionError("Event " + e.getId() + " already in favourites");
        favourites.add(e);
    }

    public void removeFavourite(Event e) {
        if (!favourites.contains(e)) throw new AssertionError("Event " + e.getId() +  " is not in favourites");
        favourites.remove(e);
    }

    public void addTrophy(Trophy t) {
        if (trophies.contains(t)) throw new AssertionError("Trophy " + t.getId() + " already in trophies");
        trophies.add(t);
    }

    public void removeTrophy(Trophy t) {
        if (!trophies.contains(t)) throw new AssertionError("Trophy " + t.getId() +  " is not in trophies");
        trophies.remove(t);
    }

    public void addFriend(User friend) {
        if (friends.contains(friend)) throw new AssertionError("Friend " + friend.getId() + " already in friends");
        friends.add(friend);
    }

    public void removeFriend(User friend) {
        if (!friends.contains(friend)) throw new AssertionError("Friend " + friend.getId() +  " is not in friends");
        friends.remove(friend);
    }
}
