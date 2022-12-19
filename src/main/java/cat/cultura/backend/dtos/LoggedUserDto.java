package cat.cultura.backend.dtos;

import java.util.ArrayList;
import java.util.List;

public class LoggedUserDto extends UserDto {

    private String userHash;
    private List<Long> favouriteEvents = new ArrayList<>();
    private List<Long> trophiesReceived = new ArrayList<>();
    private List<Long> eventsAttendance = new ArrayList<>();
    private List<Long> eventsAttended = new ArrayList<>();
    private List<Long> friendIds = new ArrayList<>();
    private List<Long> upvotedReviewIds = new ArrayList<>();

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

    public void addAttended(Long eventID) {
        eventsAttended.add(eventID);
    }

    public void addFriend(Long friendID) {
        friendIds.add(friendID);
    }
    public void addUpvotedReviews(Long reviewID) {
        upvotedReviewIds.add(reviewID);
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

    public List<Long> getEventsAttended() {
        return eventsAttended;
    }

    public void setEventsAttended(List<Long> eventsAttended) {
        this.eventsAttended = eventsAttended;
    }

    public List<Long> getUpvotedReviewIds() {
        return upvotedReviewIds;
    }

    public void setUpvotedReviewIds(List<Long> upvotedReviewIds) {
        this.upvotedReviewIds = upvotedReviewIds;
    }
}
