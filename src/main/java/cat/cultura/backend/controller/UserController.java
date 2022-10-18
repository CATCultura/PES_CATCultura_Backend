package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.repository.UserJpaRepository;
import cat.cultura.backend.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/users")
    public UserDto addUser(@RequestBody UserDto user) throws ParseException {
        User userEntity = convertUserDtoToEntity(user);
        User userCreated = service.createUser(userEntity);
        return convertUserToDto(userCreated);
    }

    @GetMapping("/users")
    public List<UserDto> findAllUsers() {
        List<User> users = service.getUsers();
        return users.stream()
                .map(this::convertUserToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        User user = service.getUserByID(id);
        return convertUserToDto(user);
    }

    @GetMapping("/users?name={name}")
    public UserDto findUserByName(@PathVariable String name) {
        User user = service.getUserByUsername(name);
        return convertUserToDto(user);
    }

    @GetMapping("/users/{id}/favourites")
    public List<EventDto> getFavouritesFromUser(@PathVariable Long id) {
        List<Event> events = service.getFavouriteEventsByID(id);
        return events.stream()
                .map(this::convertEventToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/users")
    public UserDto updateUser(@RequestBody UserDto us) throws ParseException {
        User userEntity = convertUserDtoToEntity(us);
        User user = service.updateUser(userEntity);
        return convertUserToDto(user);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id){
        return service.deleteUserByID(id);
    }

    @PutMapping("/users/{userId}/favourites")
    public String addManyToFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            FeatureCommand fc = new AddFavourite(userId,eventIds);
            fc.execute();
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }

    @DeleteMapping("/users/{userId}/favourites")
    public String removeFromFavourites(@PathVariable Long userId, @RequestBody List<Long> eventIds) {
        try {
            FeatureCommand fc = new RemoveFavourite(userId,eventIds);
            fc.execute();
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }

    @PutMapping("/users/{userId}/trophy")
    public String addManyToTrophies(@PathVariable Long userId, @RequestBody List<Long> trophiesIds) {
        try {
            FeatureCommand fc = new AddTrophy(userId,trophiesIds);
            fc.execute();
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }

    @DeleteMapping("/users/{userId}/trophy")
    public String removeFromTrophy(@PathVariable Long userId, @RequestBody List<Long> trophiesIds) {
        try {
            FeatureCommand fc = new RemoveTrophy(userId,trophiesIds);
            fc.execute();
            return "Success";
        }
        catch (AssertionError as) {
            return as.getMessage();
        }
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        //....modifications....
        return userDto;
    }

    private EventDto convertEventToDto(Event event) {
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        //....modifications....
        return eventDto;
    }

    private User convertUserDtoToEntity(UserDto userDto) throws ParseException {
        User user = modelMapper.map(userDto, User.class);
        //....modifications....
        return user;
    }

    private Event convertEventDtoToEntity(EventDto eventDto) throws ParseException {
        Event event = modelMapper.map(eventDto, Event.class);
        //....modifications....
        return event;
    }

}
