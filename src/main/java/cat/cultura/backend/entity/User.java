package cat.cultura.backend.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import javax.persistence.*;
import java.util.*;

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

    @Column(name="name_and_surname")
    private String nameAndSurname;

    @Column(name="email")
    private String email;

    @Column(name="pass")
    private String pass;

    @Column(name="creationDate")
    private String creationDate;

    @Column(name="points")
    private int points = 0;

    @ManyToMany
    @JoinTable(name="favourites",
            joinColumns = {@JoinColumn(name = "id") },
            inverseJoinColumns = {@JoinColumn(name = "eventId")}
    )
    private List<Event> favourites = new ArrayList<>();

    @ManyToMany
    @JoinTable(name="trophies",
            joinColumns = {@JoinColumn(name = "id") },
            inverseJoinColumns = {@JoinColumn(name = "trophyId")}
    )
    private List<Trophy> trophies = new ArrayList<>();

    @ManyToMany
    @JoinTable(name="attendance",
            joinColumns = {@JoinColumn(name = "id") },
            inverseJoinColumns = {@JoinColumn(name = "eventId")}
    )
    private List<Event> attendance = new ArrayList<>();

    /**
     * Set of Friend requests where requester=this and friend=other user
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "requester")
    private Set<Request> requestsTo = new HashSet<>();

    /**
     * Set of Friend requests where requester=other user and friend=this
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "friend")
    private Set<Request>  requestsFrom = new HashSet<>();

    public User(){}

    public User(String username) {
        this.username = username;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<Event> getAttendance() { return attendance; }

    public void addAttendance(Event e) {
        if(attendance.contains(e)) throw new AssertionError("Event with id: " + e.getId() + " already in attendance");
        attendance.add(e);
    }

    public void removeAttendance(Event e) {
        if (!attendance.contains(e)) throw new AssertionError("Event with id: " + e.getId() + " is not in attendance");
        attendance.remove(e);
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

    public void setAttendance(List<Event> attendance) {
        this.attendance = attendance;
    }

    public Set<Request> getFriendRequestsFor() {
        return requestsTo;
    }

    public void setFriendRequestsFor(Set<Request> requestsFor) {
        this.requestsTo = requestsFor;
    }

    public Set<Request> getFriendRequestsFrom() {
        return requestsFrom;
    }

    public void setFriendRequestsFrom(Set<Request> requestsFrom) {
        this.requestsFrom = requestsFrom;
    }

    public void addFavourite(Event e) {
        if (favourites.contains(e)) throw new AssertionError("Event with id: " + e.getId() + " already in favourites");
        favourites.add(e);
    }

    public void removeFavourite(Event e) {
        if (!favourites.contains(e)) throw new AssertionError("Event with id: " + e.getId() +  " is not in favourites");
        favourites.remove(e);
    }

    public void addTrophy(Trophy t) {
        if (trophies.contains(t)) throw new AssertionError("Trophy with id: " + t.getId() + " already in trophies");
        trophies.add(t);
    }

    public void removeTrophy(Trophy t) {
        if (!trophies.contains(t)) throw new AssertionError("Trophy with id: " + t.getId() +  " is not in trophies");
        trophies.remove(t);
    }

    public void addFriendRequestTo(Request fd) {
        if (requestsTo.contains(fd)) throw new AssertionError("Request is already exists");
        requestsTo.add(fd);
    }

    public void removeFriendRequestTo(Request fd) {
        if (!requestsTo.contains(fd)) throw new AssertionError("Request does not exist");
        requestsTo.add(fd);
    }

    public void addFriendRequestFrom(Request fd) {
        if (requestsFrom.contains(fd)) throw new AssertionError("Request is already exists");
        requestsFrom.add(fd);
    }

    public void removeFriendRequestFrom(Request fd) {
        if (!requestsFrom.contains(fd)) throw new AssertionError("Request does not exist");
        requestsFrom.add(fd);
    }

    public List<User> getRequestFrom(){
        List<User> users = new ArrayList<>();
        for (Request f: requestsFrom) {
            boolean friend = false;
            User req = f.getRequester();
            for(Request f1: requestsTo){
                if(f1.getFriend()==req) {
                    friend = true;
                    break;
                }
            }
            if(!friend) users.add(req);
        }
        return users;
    }

    public List<User> getRequestTo(){
        List<User> users = new ArrayList<>();
        for (Request f: requestsTo) {
            boolean friend = false;
            User req = f.getFriend();
            for(Request f1: requestsFrom){
                if(f1.getRequester()==req) {
                    friend = true;
                    break;
                }
            }
            if(!friend) users.add(req);
        }
        return users;
    }

    public List<User> getFriends(){
        List<User> users = new ArrayList<>();
        for (Request f: requestsTo) {
            boolean friend = false;
            User req = f.getFriend();
            for(Request f1: requestsFrom){
                if(f1.getRequester()==req) {
                    friend = true;
                    break;
                }
            }
            if(friend) users.add(req);
        }
        return users;
    }
}
