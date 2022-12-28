package cat.cultura.backend.dto;

import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class UserDtoTest {
    private ModelMapper modelMapper = new ModelMapper();

//    @Test
//    public void whenConvertUserEntityToUserDto_thenCorrect() {
//        User user = new User();
//        user.setUsername("nicolascage");
//        user.setNameAndSurname("Nicolás Jaula");
//
//        UserDto userDto = modelMapper.map(user, UserDto.class);
//        assertEquals(user.getId(), userDto.getId());
//        assertEquals(user.getUsername(), userDto.getUsername());
//        assertEquals(user.getNameAndSurname(), userDto.getNameAndSurname());
//    }
//
//    @Test
//    public void whenConvertUserDtoToUserEntity_thenCorrect() {
//        UserDto userDto = new UserDto();
//        userDto.setUsername("stevewonder");
//        userDto.setNameAndSurname("Estabancito Maravilla");
//
//        User user = modelMapper.map(userDto, User.class);
//        assertEquals(userDto.getId(), user.getId());
//        assertEquals(userDto.getUsername(), user.getUsername());
//        assertEquals(userDto.getNameAndSurname(), user.getNameAndSurname());
//    }
}
