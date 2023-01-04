package cat.cultura.backend.service.user;

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

    public List<Trophy> addTrophy(Long userId, List<Long> trophyIds) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Trophy> trophies = trophyRepo.findAllById(trophyIds);
        for (Trophy t : trophies) user.addTrophy(t);
        userRepo.save(user);
        return user.getTrophies();
    }

    public List<Trophy> removeTrophy(Long userId, List<Long> trophyIds) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Trophy> trophies = trophyRepo.findAllById(trophyIds);
        for (Trophy t : trophies) user.removeTrophy(t);
        userRepo.save(user);
        return user.getTrophies();
    }

    public List<Trophy> getTrophiesById(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getTrophies();
    }

    private void achievementManager(Long userId, Trophy trophy) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Trophy> trophies = user.getTrophies();
        if(!trophies.contains(trophy)) {
            user.addTrophy(trophy);
            userRepo.save(user);
        }
    }
    public void firstFavourite(Long userId) {
        String name = "Mark an event as Favourite";
        Trophy trophy = trophyRepo.findByName(name).orElse(null);
        if(trophy == null) {
            trophy = new Trophy();
            trophy.setName(name);
            trophy.setPoints(10);
            trophy.setDescription("You have marked your first Favourite!");
            trophyRepo.save(trophy);
        }
        achievementManager(userId, trophy);
    }

    public void firstAttendance(Long userId) {
        String name = "Mark the attendance of an event";
        Trophy trophy = trophyRepo.findByName(name).orElse(null);
        if(trophy == null) {
            trophy = new Trophy();
            trophy.setName(name);
            trophy.setPoints(10);
            trophy.setDescription("You have marked your first Attendance!");
            trophyRepo.save(trophy);
        }
        achievementManager(userId, trophy);
    }

    public void firstReview(Long userId) {
        String name = "Make a Review";
        Trophy trophy = trophyRepo.findByName(name).orElse(null);
        if(trophy == null) {
            trophy = new Trophy();
            trophy.setName(name);
            trophy.setPoints(10);
            trophy.setDescription("You have made your first Review!");
            trophyRepo.save(trophy);
        }
        achievementManager(userId, trophy);
    }

    public void firstRoute(Long userId) {
        String name = "Make a Route";
        Trophy trophy = trophyRepo.findByName(name).orElse(null);
        if(trophy == null) {
            trophy = new Trophy();
            trophy.setName(name);
            trophy.setPoints(10);
            trophy.setDescription("You have made your first Route!");
            trophyRepo.save(trophy);
        }
        achievementManager(userId, trophy);
    }

    public void createAccount(Long userId) {
        String name = "Create an Account";
        Trophy trophy = trophyRepo.findByName(name).orElse(null);
        if(trophy == null) {
            trophy = new Trophy();
            trophy.setName(name);
            trophy.setPoints(10);
            trophy.setDescription("You have joined CatCultura!");
            trophyRepo.save(trophy);
        }
        achievementManager(userId, trophy);
    }

}
