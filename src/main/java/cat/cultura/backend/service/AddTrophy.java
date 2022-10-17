package cat.cultura.backend.service;

import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.factories.RepoFactory;

import java.util.List;

public class AddTrophy implements FeatureCommand {
    public final long userID;

    public final List<Long> trophiesIDs;


    public AddTrophy(long userId, List<Long> trophiesIds) {
        this.userID = userId;
        this.trophiesIDs = trophiesIds;
    }


    @Override
    public void execute() {
        User user = RepoFactory.getInstance().getUserRepo().findById(userID).orElseThrow(UserNotFoundException::new);
        List<Trophy> trophies = RepoFactory.getInstance().getTrophyRepo().findAllById(trophiesIDs);
        for (Trophy e : trophies) {
            user.addTrophy(e);
        }
        RepoFactory.getInstance().getUserRepo().save(user);
    }
}
