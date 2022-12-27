package cat.cultura.backend.mappers;

import cat.cultura.backend.dtos.LoggedUserDto;
import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Role;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.entity.tag.TagAltresCategories;
import cat.cultura.backend.entity.tag.TagAmbits;
import cat.cultura.backend.entity.tag.TagCategories;
import com.mysql.cj.log.Log;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    private User userEntity;
    private UserDto userDto;

    private LoggedUserDto loggedUserDto;

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        User ue = new User("Joan");
        ue.addTag(new TagAmbits("arts-plastiques"));
        ue.addTag(new TagCategories("exposicions"));
        ue.addTag(new TagAltresCategories("nadal"));
        ue.setId(123L);
        ue.setPassword("1234");
        ue.setCreationDate("yesterday");
        ue.setNameAndSurname("Miquel del Roig");
        ue.setEmail("at@at.at");
        ue.setUrl("httpasja");
        ue.setRole(Role.USER);
        ue.setTelefon("1234455");
        Event fav = new Event();
        fav.setId(12L);
        User friend = new User();
        friend.setId(12L);
        ue.getFavourites().add(fav);
        ue.getAttendance().add(fav);
        ue.getFriends().add(friend);
        this.userEntity = ue;

        UserDto udto = new UserDto();
        udto.setUsername("Joan");
        udto.setId(123L);
        udto.setPassword(null);
        udto.setCreationDate("yesterday");
        udto.setNameAndSurname("Miquel del Roig");
        udto.setEmail("at@at.at");
        Set<String> t = new HashSet<>();
        t.add("arts-plastiques");
        t.add("exposicions");
        t.add("nadal");
        udto.setRole(Role.USER);
        udto.setTags(t);
        this.userDto = udto;

        LoggedUserDto ldto = new LoggedUserDto();
        ldto.setUsername("Joan");
        ldto.setId(123L);
        ldto.setPassword(null);
        ldto.setCreationDate("yesterday");
        ldto.setNameAndSurname("Miquel del Roig");
        ldto.setEmail("at@at.at");
        Set<String> t1 = new HashSet<>();
        t1.add("arts-plastiques");
        t1.add("exposicions");
        t1.add("nadal");
        ldto.setRole(Role.USER);
        ldto.setTags(t1);
        ldto.addFavourite(12L);
        ldto.addAttendance(12L);
        ldto.addFriend(12L);
        this.loggedUserDto = ldto;

    }

    @Test
    void convertUserToDto() {
        UserDto result = userMapper.convertUserToDto(this.userEntity);
        Assertions.assertEquals(userDto,result);
    }

    @Test
    void convertUserToLoggedUserDto() {
        LoggedUserDto result = userMapper.convertUserToLoggedUserDto(this.userEntity);
        Assertions.assertEquals(loggedUserDto,result);
    }

    @Test
    void convertUserDtoToEntity() {
        User result = userMapper.convertUserDtoToEntity(this.userDto);
        Assertions.assertEquals(this.userEntity,result);

    }
}