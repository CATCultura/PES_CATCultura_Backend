package cat.cultura.backend.controller;

import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.service.TrophyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TrophyController {

    @Autowired
    private TrophyService trophyService;

    @PostMapping("/trophies")
    public ResponseEntity<Trophy> addTrophy(@RequestBody Trophy t) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trophyService.saveTrophy(t));
    }

    @GetMapping("/trophies")
    public ResponseEntity<List<Trophy>> getTrophies() {
        return ResponseEntity.status(HttpStatus.OK).body(trophyService.getTrophies());
    }

    @DeleteMapping("/trophies/{id}")
    public ResponseEntity<String> removeTrophy(@PathVariable Long id){
        trophyService.deleteTrophy(id);
        return ResponseEntity.status(HttpStatus.OK).body("Trophy with id " + id + " deleted.");
    }

}
