package cat.cultura.backend.service;

import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.TrophyJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTrophyService {

    private final TrophyJpaRepository trophyRepo;

    private final UserJpaRepository userRepo;

    public UserTrophyService(UserJpaRepository userRepo, TrophyJpaRepository trophyRepo) {
        this.userRepo = userRepo;
        this.trophyRepo = trophyRepo;
    }

    public void addTrophy(Long userID, List<Long> trophyIDs) {
        User user = userRepo.findById(userID).orElseThrow(UserNotFoundException::new);
        List<Trophy> trophies = trophyRepo.findAllById(trophyIDs);
        for (Trophy e : trophies) {
            user.addTrophy(e);
        }
        userRepo.save(user);
    }

    public void removeTrophy(Long userID, List<Long> trophyIDs) {
        User user = userRepo.findById(userID).orElseThrow(UserNotFoundException::new);
        List<Trophy> trophies = trophyRepo.findAllById(trophyIDs);
        for (Trophy t : trophies) {
            user.removeTrophy(t);
        }
        userRepo.save(user);
    }

    public List<Trophy> getTrophiesByID(Long userID) {
        User user = userRepo.findById(userID).orElseThrow(UserNotFoundException::new);
        return user.getTrophies();
    }

}
