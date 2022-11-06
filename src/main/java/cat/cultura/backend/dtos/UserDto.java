package cat.cultura.backend.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class UserDto {
    private Long id;
    private String username;
    private String nameAndSurname;
    private String email;
    private String password;
    private String creationDate;
    private int points;
//    private List<Event> favourites = new ArrayList<>();
//    private List<Trophy> trophies = new ArrayList<>();
//    private List<Event> attendance = new ArrayList<>();
//    private List<User> friends = new ArrayList<>();

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

//    public List<Event> getFavourites() {
//        return favourites;
//    }
//
//    public void setFavourites(List<Event> favourites) {
//        this.favourites = favourites;
//    }
//
//    public List<Trophy> getTrophies() {
//        return trophies;
//    }
//
//    public void setTrophies(List<Trophy> trophies) {
//        this.trophies = trophies;
//    }
//
//    public List<Event> getAttendance() {
//        return attendance;
//    }
//
//    public void setAttendance(List<Event> attendance) {
//        this.attendance = attendance;
//    }
//
//    public List<User> getFriends() {
//        return friends;
//    }
//
//    public void setFriends(List<User> friends) {
//        this.friends = friends;
//    }

}
