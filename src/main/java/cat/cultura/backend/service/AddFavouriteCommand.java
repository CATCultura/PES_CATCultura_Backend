package cat.cultura.backend.service;

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




    }
}
