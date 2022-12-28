package cat.cultura.backend.mappers;

import cat.cultura.backend.dtos.LoggedUserDto;
import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.*;
import cat.cultura.backend.entity.tag.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private Set<String> extractTags(Set<Tag> userTags) {
        Set<String> toSendTags = new HashSet<>();
        if (userTags != null) {
            for (Tag t : userTags) {
                toSendTags.add(t.getValue());
            }
        }
        return toSendTags;
    }

    public UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        userDto.setTags(extractTags(user.getTags()));

        return userDto;
    }

    public LoggedUserDto convertUserToLoggedUserDto(User user) {
        LoggedUserDto userDto = modelMapper.map(user, LoggedUserDto.class);
        userDto.setPassword(null);
        userDto.setTags(extractTags(user.getTags()));
        for (Event e : user.getFavourites()) {
            userDto.addFavourite(e.getId());
        }
        for (Event e : user.getAttendance()) {
            userDto.addAttendance(e.getId());
        }
        for (Event e : user.getAttended()) {
            userDto.addAttended(e.getId());
        }
        for (User u : user.getFriends()) {
            userDto.addFriend(u.getId());
        }
        for (Review r : user.getUpvotedReviews()) {
            userDto.addUpvotedReviews(r.getId());
        }
        for (Trophy t : user.getTrophies()) {
            userDto.addTrophy(t.getId());
        }
        return userDto;
    }

    public User convertUserDtoToEntity(UserDto userDto) {
        if (userDto.getRole() == Role.ORGANIZER) {
            return  modelMapper.map(userDto, Organizer.class);
        }
        else {
            return modelMapper.map(userDto, User.class);
        }

    }


}
