package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.factories.RepoFactory;

import java.util.List;


public class AddFavourite implements FeatureCommand {

    private final long userID;

    private final List<Long> eventIDs;


    public AddFavourite(long userId, List<Long> eventIds) {
        this.userID = userId;
        this.eventIDs = eventIds;
    }


    @Override
    public void execute() {
        User user = RepoFactory.getInstance().getUserRepo().findById(userID).orElseThrow(UserNotFoundException::new);
        List<Event> events = RepoFactory.getInstance().getEventRepo().findAllById(eventIDs);
        for (Event e : events) {
            user.addFavourite(e);
        }

    }
}
