package cat.cultura.backend.service;

import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.factories.RepoFactory;

import java.util.List;

public class RemoveTrophy implements FeatureCommand{
    private final long userID;

    private final List<Long> trophyIDs;


    public RemoveTrophy(long userId, List<Long> trophyIDs) {
        this.userID = userId;
        this.trophyIDs = trophyIDs;
    }


    @Override
    public void execute() {
        User user = RepoFactory.getInstance().getUserRepo().findById(userID).orElseThrow(UserNotFoundException::new);
        List<Trophy> trophies = RepoFactory.getInstance().getTrophyRepo().findAllById(trophyIDs);
        for (Trophy t : trophies) {
            user.removeTrophy(t);
        }
        RepoFactory.getInstance().getUserRepo().save(user);
    }
}
