package cat.cultura.backend.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoggedUserDto extends UserDto {

    private String userHash;
    private List<Long> favouriteEvents = new ArrayList<>();
    private List<Long> trophiesReceived = new ArrayList<>();
    private List<Long> eventsAttendance = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoggedUserDto that)) return false;
        if (!super.equals(o)) return false;
        return favouriteEvents.equals(that.favouriteEvents) && trophiesReceived.equals(that.trophiesReceived) && eventsAttendance.equals(that.eventsAttendance) && eventsAttended.equals(that.eventsAttended) && friendIds.equals(that.friendIds) && upvotedReviewIds.equals(that.upvotedReviewIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), favouriteEvents, trophiesReceived, eventsAttendance, eventsAttended, friendIds, upvotedReviewIds);
    }

    private List<Long> eventsAttended = new ArrayList<>();
    private List<Long> friendIds = new ArrayList<>();

    private List<Long> receivedRequests = new ArrayList<>();

    private List<Long> sentRequests = new ArrayList<>();
    private List<Long> upvotedReviewIds = new ArrayList<>();

    private List<Long> reportedReviewIds = new ArrayList<>();

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
    public void addReportedReviews(Long reviewID) {
        reportedReviewIds.add(reviewID);
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

    public List<Long> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<Long> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public List<Long> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<Long> sentRequests) {
        this.sentRequests = sentRequests;
    }
}
