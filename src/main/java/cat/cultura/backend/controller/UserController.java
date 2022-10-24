package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.service.*;
import cat.cultura.backend.service.AttendanceService;
import cat.cultura.backend.exceptions.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FavouriteService favouriteService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserTrophyService userTrophyService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/users")
    public UserDto addUser(@RequestBody UserDto user) throws ParseException {
        User userEntity = convertUserDtoToEntity(user);
        User userCreated = userService.createUser(userEntity);
        return convertUserToDto(userCreated);
    }

    @GetMapping("/users")
    public List<UserDto> getAllByQuery(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "name-and-surname", required = false) String nameAndSurname,
            Pageable pageable
    ) {
        Page<User> users = userService.getByQuery(id, username, nameAndSurname, pageable);
        if(users.isEmpty()) throw new UserNotFoundException();
        return users.stream()
                .map(this::convertUserToDto)
                .toList();
    }

    @GetMapping("/users/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        User user = userService.getUserByID(id);
        return convertUserToDto(user);
    }

    @GetMapping("/users/name={name}")
    public UserDto findUserByName(@PathVariable String name) {
        User user = userService.getUserByUsername(name);
        return convertUserToDto(user);
    }

    @GetMapping("/users/{id}/favourites")
    public List<EventDto> getFavouritesFromUser(@PathVariable Long id) {
        List<Event> events = userService.getFavouriteEventsByID(id);
        return events.stream()
                .map(this::convertEventToDto)
                .toList();
    }

    @GetMapping("/user/{id}/attendance")
    public List<EventDto> getAttendanceFromUser(@PathVariable Long id) {
        List<Event> events = userService.getAttendanceEventsByID(id);
        return events.stream().map(this::convertEventToDto).toList();
    }

    @DeleteMapping("/users/{userId}/assistance")
    public String removeFromAssistance(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            attendanceService.removeAttendance(userId, eventIds);
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
        return "Success";
    }

    @PutMapping("/users/{userId}/assistance")
    public String addManyToAssistance(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            attendanceService.addAttendance(userId,eventIds);
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }


    @PutMapping("/users")
    public UserDto updateUser(@RequestBody UserDto us) {
        User userEntity = convertUserDtoToEntity(us);
        User user = userService.updateUser(userEntity);
        return convertUserToDto(user);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        return userService.deleteUserByID(id);
    }

    @PutMapping("/users/{userId}/favourites")
    public String addManyToFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            favouriteService.addFavourite(userId,eventIds);
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }

    @DeleteMapping("/users/{userId}/favourites")
    public String removeFromFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            favouriteService.removeFavourite(userId, eventIds);
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
        return "Success";
    }

    @PutMapping("/users/{userId}/trophy")
    public String addManyToTrophies(@PathVariable Long userId, @RequestBody List<Long> trophiesIds) {
        try {
            userTrophyService.addTrophy(userId,trophiesIds);
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
        return "Success";
    }

    @DeleteMapping("/users/{userId}/trophy")
    public String removeFromTrophy(@PathVariable Long userId, @RequestBody List<Long> trophiesIds) {
        try {
            userTrophyService.removeTrophy(userId,trophiesIds);
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
        return "Success";
    }

    @PutMapping("/users/{userId}/friends")
    public String addManyToFriends(@PathVariable Long userId, @RequestBody List<Long> friendsIds) {
        try {
            friendService.addFriends(userId,friendsIds);
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
        return "Success";
    }

    @DeleteMapping("/users/{userId}/friends")
    public String removeFriend(@PathVariable Long userId, @RequestBody List<Long> friendsIds) {
        try {
            friendService.removeFriends(userId,friendsIds);
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
        return "Success";
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        return userDto;
    }

    private EventDto convertEventToDto(Event event) {
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        //....modifications....
        return eventDto;
    }

    private User convertUserDtoToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        //....modifications....
        return user;
    }

    private Event convertEventDtoToEntity(EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        //....modifications....
        return event;
    }

}
