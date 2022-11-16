package cat.cultura.backend.services;

import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.repository.TrophyJpaRepository;
import cat.cultura.backend.service.TrophyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class TrophyServiceTest {
    @Autowired
    TrophyService trophyService;

    @MockBean
    TrophyJpaRepository trophyJpaRepository;

    @Test
    public void saveTrophyTest() {
        Trophy trophy = new Trophy();
        trophy.setId(3L);
        given(trophyJpaRepository.save(trophy)).willReturn(trophy);

        Trophy result = trophyService.saveTrophy(trophy);
        Assertions.assertEquals(trophy, result);
    }

    @Test
    public void getTrophiesTest() {
        Trophy trophy = new Trophy();
        trophy.setId(3L);
        List<Trophy> trophies = new ArrayList<>();
        trophies.add(trophy);
        given(trophyJpaRepository.findAll()).willReturn(trophies);

        List<Trophy> result = trophyService.getTrophies();
        Assertions.assertEquals(trophies, result);
    }

}
