package cat.cultura.backend.entity;

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
    @Column(name="name", unique = true)
    private String name;

    @Column(name="creationDate")
    private String creationDate;
    @ElementCollection
    @CollectionTable(name="favourites", joinColumns=@JoinColumn(name="id"))
    @Column(name="favourites")
    private List<Event> favourites = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public User(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (favourites.contains(e)) throw new AssertionError("Event in favourites");
        favourites.add(e);
    }

    public void removeFavourite(Event e) {
        if (!favourites.contains(e)) throw new AssertionError("Event is not in favourites");
        favourites.remove(e);
    }
}
