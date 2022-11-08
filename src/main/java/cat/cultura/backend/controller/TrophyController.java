package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.dtos.TrophyDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.service.TrophyService;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @PostMapping("/trophies")
    public ResponseEntity<TrophyDto> addTrophy(@RequestBody TrophyDto t) {
        Trophy trophy = convertTrophyDtoToEntity(t);
        trophy = trophyService.saveTrophy(trophy);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertTrophyToDto(trophy));
    }

    @GetMapping("/trophies")
    public ResponseEntity<List<TrophyDto>> getTrophies() {
        List<Trophy> trophies = trophyService.getTrophies();
        return ResponseEntity.status(HttpStatus.OK).body(trophies.stream().map(this::convertTrophyToDto).collect(Collectors.toList()));
    }

    @DeleteMapping("/trophies/{id}")
    public ResponseEntity<String> removeTrophy(@PathVariable Long id){
        trophyService.deleteTrophy(id);
        return ResponseEntity.status(HttpStatus.OK).body("Trophy with id " + id + " deleted.");
    }

    private TrophyDto convertTrophyToDto(Trophy trophy) {
        TrophyDto trophyDto = modelMapper.map(trophy, TrophyDto.class);
        //....modifications....
        return trophyDto;
    }

    private Trophy convertTrophyDtoToEntity(TrophyDto trophyDto) {
        Trophy trophy = modelMapper.map(trophyDto, Trophy.class);
        //....modifications....
        return trophy;
    }

}
