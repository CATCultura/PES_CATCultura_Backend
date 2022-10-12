package cat.cultura.backend.entity;

import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AddFavouriteCommand implements FeatureCommand {


    @Autowired
    public UserJpaRepository repoUser;
    @Autowired
    public EventJpaRepository repoEvent;

    @Override
    public void execute() {

    }
}
