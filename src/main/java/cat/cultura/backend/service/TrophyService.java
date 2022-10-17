package cat.cultura.backend.service;

import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.repository.TrophyJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrophyService {

    private final TrophyJpaRepository trophyRepo;

    public TrophyService(TrophyJpaRepository trophyRepo) {
        this.trophyRepo = trophyRepo;
    }

    public Trophy saveTrophy(Trophy trophy) {
        return trophyRepo.save(trophy);
    }

    public List<Trophy> saveTrophies(List<Trophy> trophies) {
        return trophyRepo.saveAll(trophies);
    }

    public List<Trophy> getTrophies() {
        return trophyRepo.findAll();
    }

    public Trophy getTrophy(Long id) {
        return trophyRepo.findById(id).orElse(null);
    }
}
