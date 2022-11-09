package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.dtos.TrophyDto;
import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Trophy;
import cat.cultura.backend.entity.User;
import cat.cultura.backend.service.*;
import cat.cultura.backend.service.AttendanceService;
import cat.cultura.backend.exceptions.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private RequestService requestService;

    @Autowired
    private ModelMapper modelMapper;

    //Post, Get, Put, and Delete for all UserDto properties (see class UserDto)

    @PostMapping("/users")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto user) {
        User userEntity = convertUserDtoToEntity(user);
        User userCreated = userService.createUser(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertUserToDto(userCreated));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsersByQuery(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "name-and-surname", required = false) String nameAndSurname,
            Pageable pageable
    ) {
        Page<User> users = userService.getUsersByQuery(id, username, nameAndSurname, pageable);
        if(users.isEmpty()) throw new UserNotFoundException();
        return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(this::convertUserToDto).toList());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(convertUserToDto(user));
    }

    @GetMapping("/users/name={name}")
    public ResponseEntity<UserDto> getUserByName(@PathVariable String name) {
        User user = userService.getUserByUsername(name);
        return ResponseEntity.status(HttpStatus.OK).body(convertUserToDto(user));
    }

    @PutMapping("/users")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto us) {
        User userEntity = convertUserDtoToEntity(us);
        User user = userService.updateUser(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertUserToDto(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User removed");
    }

    //Put, Get and Delete for attendance of a user

    @PutMapping("/users/{id}/attendance/{eventId}")
    public ResponseEntity<String> addAttendance(@PathVariable Long id, @PathVariable Long eventId) {
        try {
            attendanceService.addAttendance(id,eventId);
        } catch (AssertionError as) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Event attendance added");
    }

    @GetMapping("/user/{id}/attendance")
    public ResponseEntity<List<EventDto>> getAttendance(@PathVariable Long id) {
        List<Event> events = userService.getAttendanceEventsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(this::convertEventToDto).toList());
    }

    @DeleteMapping("/users/{id}/assistance/{eventId}")
    public ResponseEntity<String> removeAttendance(@PathVariable Long id, @PathVariable Long eventId) {
        try {
            attendanceService.removeAttendance(id, eventId);
        } catch (AssertionError as) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Event attendance removed");
    }

    //Put, Get and Delete for favourites of a user

    @PutMapping("/users/{id}/favourites/{eventId}")
    public ResponseEntity<String> addFavourites(@PathVariable Long id, @PathVariable Long eventId) {
        try {
            favouriteService.addFavourite(id,eventId);
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Favourite added");
    }

    @GetMapping("/users/{id}/favourites")
    public ResponseEntity<List<EventDto>> getFavourites(@PathVariable Long id) {
        List<Event> events = userService.getFavouriteEventsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(this::convertEventToDto).toList());
    }

    @DeleteMapping("/users/{id}/favourites/{eventId}")
    public ResponseEntity<String> removeFavourites(@PathVariable Long id, @PathVariable Long eventId) {
        try {
            favouriteService.removeFavourite(id, eventId);
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Favourite removed");
    }

    //Put, Get and Delete for trophies of a user

    @PutMapping("/users/{id}/trophies")
    public ResponseEntity<String> addTrophies(@PathVariable Long id, @RequestBody List<Long> trophiesIds) {
        try {
            userTrophyService.addTrophy(id,trophiesIds);
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Trophies added");
    }

    @GetMapping("/users/{id}/trophies")
    public ResponseEntity<List<TrophyDto>> getTrophies(@PathVariable Long id) {
        List<Trophy> trophies = userTrophyService.getTrophiesByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(trophies.stream().map(this::convertTrophyToDto).toList());
    }

    @DeleteMapping("/users/{id}/trophies")
    public ResponseEntity<String> removeTrophies(@PathVariable Long id, @RequestBody List<Long> trophiesIds) {
        try {
            userTrophyService.removeTrophy(id,trophiesIds);
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Trophies removed");
    }

    //Put, Get and Delete for the friend requests to other users of a user

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<String> addRequestsTo(@PathVariable Long id, @PathVariable Long friendId) {
        try {
            requestService.addFriendRequestsTo(id,friendId);
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Request added");
    }

    @GetMapping("users/{id}/friends")
    public ResponseEntity<List<UserDto>> getRequestsTo(@PathVariable Long id, @RequestParam(required = true) String status) {
        List<User> users = new ArrayList<>();
        if(Objects.equals(status, "requested")) users = requestService.getRequestsTo(id);
        else if(Objects.equals(status, "received")) users = requestService.getRequestFrom(id);
        else if(Objects.equals(status, "accepted")) users = requestService.getFriends(id);
        if(users.isEmpty()) throw new UserNotFoundException();
        return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(this::convertUserToDto).toList());
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<String> removeRequestsTo(@PathVariable Long id, @PathVariable Long friendId) {
        try {
            requestService.removeFriendRequestsTo(id,friendId);
        } catch (AssertionError as) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Friend removed");
    }

    //Dto conversion functions

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
