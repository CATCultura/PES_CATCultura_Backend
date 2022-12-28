package cat.cultura.backend.mappers;

import cat.cultura.backend.dtos.TrophyDto;
import cat.cultura.backend.entity.Trophy;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TrophyMapper {

    private final ModelMapper modelMapper;

    public TrophyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TrophyDto convertTrophyToDto(Trophy trophy) {
        TrophyDto trophyDto = modelMapper.map(trophy, TrophyDto.class);
        //....modifications....
        return trophyDto;
    }

    public Trophy convertTrophyDtoToEntity(TrophyDto trophyDto) {
        Trophy trophy = modelMapper.map(trophyDto, Trophy.class);
        //....modifications....
        return trophy;
    }
}
