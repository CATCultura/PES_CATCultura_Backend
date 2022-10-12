package cat.cultura.backend.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "User")
public class User {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="name")
    private String name;
    @ElementCollection
    @CollectionTable(name="favourites", joinColumns=@JoinColumn(name="id"))
    @Column(name="favourites")
    private List<String> favourites = new ArrayList<String>(); //id of favourite events

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

    public String getFavourites() {
        String fav = String.join(",",favourites);
        return fav;
    }

    public void setFavourites(String favourites) {
        List<String> myList = new ArrayList<String>(Arrays.asList(favourites.split(",")));
        this.favourites = myList;
    }
}
