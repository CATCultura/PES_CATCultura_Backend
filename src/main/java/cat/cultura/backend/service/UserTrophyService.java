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

    public User addTrophy(Long userId, List<Long> trophyIds) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Trophy> trophies = trophyRepo.findAllById(trophyIds);
        for (Trophy t : trophies) user.addTrophy(t);
        return userRepo.save(user);
    }

    public User removeTrophy(Long userId, List<Long> trophyIds) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Trophy> trophies = trophyRepo.findAllById(trophyIds);
        for (Trophy t : trophies) user.removeTrophy(t);
        return userRepo.save(user);
    }

    public List<Trophy> getTrophiesById(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getTrophies();
    }

    private void achivementManager(Long userId, Trophy trophy) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        List<Trophy> trophies = user.getTrophies();
        for(Trophy t : trophies) {
            if(t.getName() == trophy.getName()){
                user.addTrophy(trophy);
                break;
            }
        }
    }
    public void firstFavourite(Long userId) {
        Trophy trophy = new Trophy();
        trophy.setName("Mark a event as Favourite");
        trophy.setPoints(10);
        trophy.setDescription("You have marked your first Favourite!");
        achivementManager(userId, trophy);
    }

    public void firstAttendance(Long userId) {
        Trophy trophy = new Trophy();
        trophy.setName("Mark the attendance of an event");
        trophy.setPoints(10);
        trophy.setDescription("You have marked your first Attendance!");
        achivementManager(userId, trophy);
    }

    public void firstReview(Long userId) {
        Trophy trophy = new Trophy();
        trophy.setName("Make a Review");
        trophy.setPoints(10);
        trophy.setDescription("You have made your first Review!");
        achivementManager(userId, trophy);
    }

    public void firstRoute(Long userId) {
        Trophy trophy = new Trophy();
        trophy.setName("Make a Route");
        trophy.setPoints(10);
        trophy.setDescription("You have made your first Route!");
        achivementManager(userId, trophy);
    }

    public void createAccount(Long userId) {
        Trophy trophy = new Trophy();
        trophy.setName("Create an Account");
        trophy.setPoints(10);
        trophy.setDescription("You have joined CatCultura!");
        achivementManager(userId, trophy);
    }

}
