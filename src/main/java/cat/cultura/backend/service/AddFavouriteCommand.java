package cat.cultura.backend.service;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.factories.RepoFactory;

import java.util.List;


public class AddFavouriteCommand implements FeatureCommand {



    private long userID;

    private List<Long> eventIDs;


    public AddFavouriteCommand() {

    }
    public AddFavouriteCommand(long userId, List<Long> eventIds) {
        this.userID = userId;
        this.eventIDs = eventIds;
    }


    @Override
    public void execute() {
        RepoFactory r = RepoFactory.getInstance();
        User u = RepoFactory.getInstance().getUserRepo().findById(userID).orElseThrow(UserNotFoundException::new);
        List<Event> e = RepoFactory.getInstance().getEventRepo().findAllById(eventIDs);
        System.out.println(u.getName());



    }
}
