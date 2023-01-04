package cat.cultura.backend.entity;

import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.exceptions.TagAlreadyAddedException;
import cat.cultura.backend.exceptions.TagNotPresentException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Entity
@Table(name = "User")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
@DiscriminatorFormula("CASE when role=0 then 'User' when role=1 then 'Administrator' when role=2 then 'Organizer' when role=3 then 'User' end")
@DiscriminatorValue("User")
public class User {

    public static final String EVENT_WITH_ID = "Event with id: ";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="username", unique = true)
    private String username;

    @Column(name="role")
    private Role role = Role.USER;
    @Lob
    @Column(name="user_hash", unique = true)
    private String userHash;

    @Column(name="name_and_surname")
    private String nameAndSurname;

    @Column(name="telefon")
    private String telefon;

    @Column(name="url")
    private String url;
    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="creationDate")
    private String creationDate;

    @Column(name="points", nullable = false)
    private int points = 0;

    @Column(name="reports", nullable = false)
    private int reports = 0;

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

    @ManyToMany
    @JoinTable(name="attended",
            joinColumns = {@JoinColumn(name = "id") },
            inverseJoinColumns = {@JoinColumn(name = "eventId")}
    )
    private List<Event> attended = new ArrayList<>();

    /**
     * Set of Friend requests where requester=this and friend=other user
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "requester")
    private Set<Request> requestsSent = new HashSet<>();

    /**
     * Set of Friend requests where requester=other user and friend=this
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "friend")
    private Set<Request> requestsReceived = new HashSet<>();

    @ManyToMany
    @JoinTable(name="friendships",
            joinColumns = {@JoinColumn(name = "id") },
            inverseJoinColumns = {@JoinColumn(name = "friendId")}
    )
    private List<User> friends = new ArrayList<>();

    @ManyToMany
    private List<Tag> tagsAmbits = new ArrayList<>();

    @ManyToMany
    private List<Tag> tagsCateg = new ArrayList<>();

    @ManyToMany
    private List<Tag> tagsAltresCateg = new ArrayList<>();

    @OneToMany
    @CollectionTable(name="Routes", joinColumns=@JoinColumn(name="id"))
    @Column(name="Routes")
    private List<Route> routes = new ArrayList<>();

    @ManyToMany
    @CollectionTable(name="upvotedReviews", joinColumns=@JoinColumn(name="id"))
    @Column(name="Review")
    private List<Review> upvotedReviews = new ArrayList<>();

    @ManyToMany
    @CollectionTable(name="reportedReviews", joinColumns=@JoinColumn(name="id"))
    @Column(name="reportedReviews")
    private List<Review> reportedReviews = new ArrayList<>();

    @ManyToMany
    @CollectionTable(name="reportedUsers", joinColumns=@JoinColumn(name="id"))
    @Column(name="ReportedUsers")
    private List<User> reportedUsers = new ArrayList<>();

    public void setAttended(List<Event> attended) {
        this.attended = attended;
    }

    public List<Review> getUpvotedReviews() {

        return upvotedReviews;
    }

    public void setUpvotedReviews(List<Review> upvotedReviews) {
        this.upvotedReviews = upvotedReviews;
    }

    public void removeUpvote(Review review) {
        if(!upvotedReviews.contains(review)) throw new AssertionError("Review with id " + review.getId() + " is not in upvoted reviews");
        else upvotedReviews.remove(review);
    }

    public void addUpvote(Review review) {
        if(upvotedReviews.contains(review)) throw new AssertionError("Review with id " + review.getId() + " is already in upvoted reviews");
        else upvotedReviews.add(review);
    }

    public List<Review> getReportedReviews() {
        return reportedReviews;
    }

    public void setReportedReviews(List<Review> reportedReviews) {
        this.reportedReviews = reportedReviews;
    }

    public void removeReport(Review review) {
        if(!reportedReviews.contains(review)) throw new AssertionError("Review with id " + review.getId() + " is not in reported reviews");
        else reportedReviews.remove(review);
    }

    public void addReport(Review review) {
        if(reportedReviews.contains(review)) throw new AssertionError("Review with id " + review.getId() + " is already in reported reviews");
        else reportedReviews.add(review);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && username.equals(user.username) && nameAndSurname.equals(user.nameAndSurname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, nameAndSurname);
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<Tag> getTagsAmbits() {
        return tagsAmbits;
    }
    public void setTagsAmbits(List<Tag> tagsAmbits) {
        this.tagsAmbits = tagsAmbits;
    }

    public List<Tag> getTagsCateg() {
        return tagsCateg;
    }

    public void setTagsCateg(List<Tag> tagsCateg) {
        this.tagsCateg = tagsCateg;
    }

    public List<Tag> getTagsAltresCateg() {
        return tagsAltresCateg;
    }

    public void setTagsAltresCateg(List<Tag> tagsAltresCateg) {
        this.tagsAltresCateg = tagsAltresCateg;
    }

    public void addTag(Tag tag) {
        switch (tag.getType()) {
            case CATEGORIES -> {
                if (tagsCateg.contains(tag)) throw new TagAlreadyAddedException();
                tagsCateg.add(tag);
            }
            case AMBITS -> {
                if (tagsAmbits.contains(tag)) throw new TagAlreadyAddedException();
                tagsAmbits.add(tag);
            }
            case ALTRES_CATEGORIES -> {
                if (tagsAltresCateg.contains(tag)) throw new TagAlreadyAddedException();
                tagsAltresCateg.add(tag);
            }
        }
    }

    public Set<Tag> getTags() {
        Set<Tag> tags = new HashSet<>();
        tags.addAll(tagsAmbits);
        tags.addAll(tagsCateg);
        tags.addAll(tagsAltresCateg);
        return tags;
    }

    public void removeTag(Tag tag) {
        switch (tag.getType()) {
            case CATEGORIES -> {
                if (!tagsCateg.contains(tag)) throw new TagNotPresentException();
                tagsCateg.remove(tag);
            }
            case AMBITS -> {
                if (!tagsAmbits.contains(tag)) throw new TagNotPresentException();
                tagsAmbits.remove(tag);
            }
            case ALTRES_CATEGORIES -> {
                if (!tagsAltresCateg.contains(tag)) throw new TagNotPresentException();
                tagsAltresCateg.remove(tag);
            }
        }
    }


    public User(){
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
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

    public List<Event> getAttendance() { return attendance; }

    public void addAttendance(Event e) {
        if(attendance.contains(e)) throw new AssertionError(EVENT_WITH_ID + e.getId() + " already in attendance");
        attendance.add(e);
    }

    public void removeAttendance(Event e) {
        if (!attendance.contains(e)) throw new AssertionError(EVENT_WITH_ID + e.getId() + " is not in attendance");
        attendance.remove(e);
    }

    public List<Event> getAttended() { return attended; }

    public void addAttended(Event e) {

        if(attended.contains(e)) throw new AssertionError(EVENT_WITH_ID + e.getId() + " already in attended");
        attended.add(e);
    }

    public void removeAttended(Event e) {
        if (!attended.contains(e)) throw new AssertionError(EVENT_WITH_ID + e.getId() + " is not in attended");
        attended.remove(e);
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
        return requestsSent;
    }

    public void setFriendRequestsFor(Set<Request> requestsFor) {
        this.requestsSent = requestsFor;
    }

    public Set<Request> getFriendRequestsFrom() {
        return requestsReceived;
    }

    public void setFriendRequestsFrom(Set<Request> requestsFrom) {
        this.requestsReceived = requestsFrom;
    }

    public void addFavourite(Event e) {
        if (favourites.contains(e)) throw new AssertionError(EVENT_WITH_ID + e.getId() + " already in favourites");
        favourites.add(e);
    }

    public void removeFavourite(Event e) {
        if (!favourites.contains(e)) throw new AssertionError(EVENT_WITH_ID + e.getId() +  " is not in favourites");
        favourites.remove(e);
    }

    public void addTrophy(Trophy t) {
        if (trophies.contains(t)) throw new AssertionError("Trophy with id: " + t.getId() + " already in trophies");
        trophies.add(t);
        points = points + t.getPoints();
    }

    public void removeTrophy(Trophy t) {
        if (!trophies.contains(t)) throw new AssertionError("Trophy with id: " + t.getId() +  " is not in trophies");
        trophies.remove(t);
        points = points - t.getPoints();
    }

    public void addFriendRequestTo(Request fd) {
        if (requestsSent.contains(fd)) throw new AssertionError("Request already exists");
        requestsSent.add(fd);
    }

    public void removeFriendRequestTo(Request fd) {
        if (!requestsSent.contains(fd)) throw new AssertionError("Request does not exist");
        requestsSent.remove(fd);

    }

    public void addFriendRequestFrom(Request fd) {
        if (requestsReceived.contains(fd)) throw new AssertionError("Request already exists");
        requestsReceived.add(fd);
    }

    public void removeFriendRequestFrom(Request fd) {
        if (!requestsReceived.contains(fd)) throw new AssertionError("Request does not exist");
        requestsReceived.remove(fd);
    }

    public List<User> getRequestFrom(){
        List<User> users = new ArrayList<>();
        for (Request f: requestsReceived) {
            boolean friend = false;
            User req = f.getRequester();
            for(Request f1: requestsSent){
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
        for (Request f: requestsSent) {
            boolean friend = false;
            User req = f.getFriend();
            for(Request f1: requestsReceived){
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
        return this.friends;
    }

    public String createUserHash() {
        if (this.id == null || this.username == null)
            throw new AssertionError("Required fields are null");
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String toHash = this.id.toString()+this.username;
        this.userHash = bytesToHex(digest.digest(toHash.getBytes(StandardCharsets.UTF_8)));
        
        return this.userHash;
    }

    public String getUserHash() {
        return userHash;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Set of Friend requests where requester=this and friend=other user
     */
    public Set<Request> getRequestsSent() {
        return this.requestsSent;
    }

    /**
     * Set of Friend requests where requester=this and friend=other user
     */
    public void setRequestsSent(Set<Request> requestsSent) {
        this.requestsSent = requestsSent;
    }

    /**
     * Set of Friend requests where requester=other user and friend=this
     */
    public Set<Request> getRequestsReceived() {
        return this.requestsReceived;
    }

    /**
     * Set of Friend requests where requester=other user and friend=this
     */
    public void setRequestsReceived(Set<Request> requestsReceived) {
        this.requestsReceived = requestsReceived;
    }

    public boolean hasFriend(User friend) {
        return this.friends.contains(friend);
    }

    public void addFriend(User friend) {
        if (this.equals(friend)) throw new AssertionError();
        if (!this.friends.contains(friend)) {
            this.friends.add(friend);
            friend.addFriend(this);
        }
    }

    public void removeFriend(User friend) {
        if (this.friends.contains(friend)) {
            this.friends.remove(friend);
            friend.removeFriend(this);
        }
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public void deleteRoute(Route route) {
        if(!routes.contains(route)) throw new AssertionError("Route with id: " + route.getRouteId() + " doesn't belong to this user");
        routes.remove(route);
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void removePoints(int points) {
        this.points -= points;
    }
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getReports() {
        return reports;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public void report(){
        ++this.reports;
    }

    public void removeReport() {
        --this.reports;
    }

    public List<User> getReportedUsers() {
        return reportedUsers;
    }

    public void setReportedUsers(List<User> reportedUsers) {
        this.reportedUsers = reportedUsers;
    }

    public void removeReportToUser(User user) {
        if(!reportedUsers.contains(user)) throw new AssertionError("User with id " + user.getId() + " is not in reported users");
        else reportedUsers.remove(user);
    }

    public void addReportToUser(User user) {
        if(reportedUsers.contains(user)) throw new AssertionError("User with id " + user.getId() + " is already in reported users");
        else reportedUsers.add(user);
    }

    public List<Event> getOrganizedEvents() {
        return new ArrayList<>();
    }

    public void setOrganizedEvents(List<Event> organizedEvents) {
        // to avoid issues
    }


}
