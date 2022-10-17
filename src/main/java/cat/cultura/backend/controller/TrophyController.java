package cat.cultura.backend.controller;

import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.service.TrophyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrophyController {

    @Autowired
    private TrophyService trophyService;

    @GetMapping("/trophies")
    public List<Trophy> getTrophies() {
        return trophyService.getTrophies();
    }

    @PostMapping("/trophies")
    public Trophy addTrophy(@RequestBody Trophy t) {
        return trophyService.saveTrophy(t);
    }

}
