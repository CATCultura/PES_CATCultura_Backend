package cat.cultura.backend.factories;

import cat.cultura.backend.repository.EventJpaRepository;
import cat.cultura.backend.repository.TrophyJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoFactory {
    @Autowired
    private EventJpaRepository eventRepo;

    @Autowired
    private UserJpaRepository userRepo;

    @Autowired
    private TrophyJpaRepository trophyRepo;

    private static RepoFactory instance;


    private RepoFactory() {
        instance = this;
    }

    public static RepoFactory getInstance() {
        return instance;
    }


    public UserJpaRepository getUserRepo() {
        return userRepo;
    }


    public EventJpaRepository getEventRepo() {
        return eventRepo;
    }

    public TrophyJpaRepository getTrophyRepo() {
        return trophyRepo;
    }

    public void initialize() {
    }
}
