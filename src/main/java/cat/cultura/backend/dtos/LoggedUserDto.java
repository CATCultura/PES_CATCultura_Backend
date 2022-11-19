package cat.cultura.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class LoggedUserDto extends UserDto {

    private String userHash;


    private List<Long> favouriteEvents = new ArrayList<>();
    private List<Long> trophiesReceived = new ArrayList<>();



    private List<Long> eventsAttendance = new ArrayList<>();
    private List<Long> friendIds = new ArrayList<>();

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public void addFavourite(Long eventID) {
        favouriteEvents.add(eventID);
    }

    public void addTrophy(Long trophyID) {
        trophiesReceived.add(trophyID);
    }

    public void addAttendance(Long eventID) {
        eventsAttendance.add(eventID);
    }

    public void addFriend(Long friendID) {
        friendIds.add(friendID);
    }

    public List<Long> getTrophiesReceived() {
        return trophiesReceived;
    }

    public void setTrophiesReceived(List<Long> trophiesReceived) {
        this.trophiesReceived = trophiesReceived;
    }

    public List<Long> getEventsAttendance() {
        return eventsAttendance;
    }

    public void setEventsAttendance(List<Long> eventsAttendance) {
        this.eventsAttendance = eventsAttendance;
    }

    public List<Long> getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(List<Long> friendIds) {
        this.friendIds = friendIds;
    }

    public List<Long> getFavouriteEvents() {
        return favouriteEvents;
    }

    public void setFavouriteEvents(List<Long> favouriteEvents) {
        this.favouriteEvents = favouriteEvents;
    }

}
