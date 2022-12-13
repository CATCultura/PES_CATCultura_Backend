package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.TrophyDto;
import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.mappers.TrophyMapper;
import cat.cultura.backend.service.TrophyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TrophyController {

    @Autowired
    private TrophyService trophyService;

    @Autowired
    private TrophyMapper trophyMapper;

    @PostMapping("/trophies")
    public ResponseEntity<TrophyDto> addTrophy(@RequestBody TrophyDto t) {
        Trophy trophy = trophyMapper.convertTrophyDtoToEntity(t);
        trophy = trophyService.saveTrophy(trophy);
        return ResponseEntity.status(HttpStatus.CREATED).body(trophyMapper.convertTrophyToDto(trophy));
    }

    @GetMapping("/trophies")
    public ResponseEntity<List<TrophyDto>> getTrophies() {
        List<Trophy> trophies = trophyService.getTrophies();
        return ResponseEntity.status(HttpStatus.OK).body(trophies.stream().map(trophyMapper::convertTrophyToDto).collect(Collectors.toList()));
    }

    @DeleteMapping("/trophies/{id}")
    public ResponseEntity<String> removeTrophy(@PathVariable Long id){
        trophyService.deleteTrophy(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
