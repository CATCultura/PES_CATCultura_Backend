package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.*;
import cat.cultura.backend.entity.*;
import cat.cultura.backend.mappers.ReviewMapper;
import cat.cultura.backend.mappers.RouteMapper;
import cat.cultura.backend.mappers.TrophyMapper;
import cat.cultura.backend.service.*;
import cat.cultura.backend.service.AttendanceService;
import cat.cultura.backend.interceptors.CurrentUser;
import cat.cultura.backend.mappers.EventMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private ReviewService reviewService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private TrophyMapper trophyMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RouteService routeService;


    @GetMapping("/login")
    public ResponseEntity<LoggedUserDto> authenticate() {
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = userService.getUserByUsername(currentUser.getUsername());
        LoggedUserDto loggedUserDto = convertUserToLoggedUserDto(u);
        return new ResponseEntity<>(loggedUserDto,HttpStatus.OK);
    }


    //Post, Get, Put, and Delete for all UserDto properties (see class UserDto)
    @PostMapping("/users")
    public ResponseEntity<LoggedUserDto> addUser(@RequestBody UserDto user) {
        User userEntity = convertUserDtoToEntity(user);
        User userCreated = userService.createUser(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertUserToLoggedUserDto(userCreated));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsersByQuery(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "name-and-surname", required = false) String nameAndSurname,
            Pageable pageable
    ) {
//
        Page<User> users = userService.getUsersByQuery(id, username, nameAndSurname, pageable);
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
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Put, Get and Delete for attendance of a user

    @PutMapping("/users/{id}/attendance/{eventId}")
    public ResponseEntity<String> addAttendance(@PathVariable Long id, @PathVariable Long eventId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                attendanceService.addAttendance(id, eventId);
            } catch (AssertionError as) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
            }
            userTrophyService.firstAttendance(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Event attendance added");
        }
    }

    @GetMapping("/users/{id}/attendance")
    public ResponseEntity<List<EventDto>> getAttendance(@PathVariable Long id) {
        List<Event> events = userService.getAttendanceEventsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @DeleteMapping("/users/{id}/assistance/{eventId}")
    public ResponseEntity<String> removeAttendance(@PathVariable Long id, @PathVariable Long eventId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                attendanceService.removeAttendance(id, eventId);
            } catch (AssertionError as) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    //Put, Get and Delete for favourites of a user

    @PutMapping("/users/{id}/favourites/{eventId}")
    public ResponseEntity<String> addFavourites(@PathVariable Long id, @PathVariable Long eventId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                favouriteService.addFavourite(id, eventId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
            }
            userTrophyService.firstFavourite(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Favourite added");
        }
    }

    @GetMapping("/users/{id}/favourites")
    public ResponseEntity<List<EventDto>> getFavourites(@PathVariable Long id) {
        List<Event> events = userService.getFavouriteEventsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @DeleteMapping("/users/{id}/favourites/{eventId}")
    public ResponseEntity<String> removeFavourites(@PathVariable Long id, @PathVariable Long eventId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                favouriteService.removeFavourite(id, eventId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    //Put, Get and Delete for trophies of a user

    @PutMapping("/users/{id}/trophies")
    public ResponseEntity<UserDto> addTrophies(@PathVariable Long id, @RequestBody List<Long> trophiesIds) {
        User user;
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                user = userTrophyService.addTrophy(id, trophiesIds);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(convertUserToDto(user));
        }
    }

    @GetMapping("/users/{id}/trophies")
    public ResponseEntity<List<TrophyDto>> getTrophies(@PathVariable Long id) {
        List<Trophy> trophies = userTrophyService.getTrophiesById(id);
        return ResponseEntity.status(HttpStatus.OK).body(trophies.stream().map(trophyMapper::convertTrophyToDto).toList());
    }

    @DeleteMapping("/users/{id}/trophies")
    public ResponseEntity<UserDto> removeTrophies(@PathVariable Long id, @RequestBody List<Long> trophiesIds) {
        User user;
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                user = userTrophyService.removeTrophy(id, trophiesIds);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(convertUserToDto(user));
        }
    }

    //Put, Get and Delete for the friend requests to other users of a user

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<String> addRequestsTo(@PathVariable Long id, @PathVariable Long friendId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                requestService.addFriend(id, friendId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Request added");
        }
    }

    @GetMapping("users/{id}/friends")
    public ResponseEntity<List<UserDto>> getRequestsTo(@PathVariable Long id, @RequestParam(required = true) String status) {
        List<User> users = new ArrayList<>();
        if(Objects.equals(status, "requested")) users = requestService.getRequestsTo(id);
        else if(Objects.equals(status, "received")) users = requestService.getRequestFrom(id);
        else if(Objects.equals(status, "accepted")) users = requestService.getFriends(id);
        return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(this::convertUserToDto).toList());
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity removeRequestsTo(@PathVariable Long id, @PathVariable Long friendId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                requestService.removeFriend(id, friendId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @GetMapping("/users/generate_route")
    public ResponseEntity<List<EventDto>> getRoutes(
            @RequestParam(required = true) double lat,
            @RequestParam(required = true) double lon,
            @RequestParam(required = true) int radius,
            @RequestParam(required = true) String day,
            @RequestParam(required = false) Long userId
    ) {
        List<Event> events;
        if(userId != null) {
            events = routeService.getUserAdjustedRouteInADayAndLocation(lat, lon, radius, day, userId);
            userTrophyService.firstRoute(userId);
        } else {
            events = routeService.getRouteInADayAndLocation(lat, lon, radius, day);
        }
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @GetMapping("/users/{id}/routes")
    public ResponseEntity<List<RouteDto>> getRoutes(@PathVariable Long id) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            List<Route> routes = routeService.getRoutes(id);
            return ResponseEntity.status(HttpStatus.OK).body(routes.stream().map(routeMapper::convertRouteToDto).toList());
        }
    }

    @PostMapping("/users/{id}/routes")
    public ResponseEntity<RouteDto> saveRoute(@PathVariable Long id, @RequestBody List<EventDto> events) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            List<Event> ev = events.stream().map(eventMapper::convertEventDtoToEntity).toList();
            Route result = routeService.saveRoute(ev,id);
            return ResponseEntity.status(HttpStatus.CREATED).body(routeMapper.convertRouteToDto(result));
        }
    }

    @DeleteMapping("/users/{userId}/routes/{routeId}")
    public ResponseEntity getEventsRoute(@PathVariable Long userId, @PathVariable Long routeId) {
        if(!isCurrentUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                routeService.deleteRoute(routeId,userId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(as.getMessage());
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews.stream().map(reviewMapper::convertReviewToDto).toList());
    }

    @PostMapping("/users/{userId}/reviews")
    public ResponseEntity<ReviewDto> addEvent(
            @PathVariable Long userId,
            @RequestParam(required = true) Long eventId,
            @RequestBody ReviewDto ev) {
        ReviewDto reviewDto;
        if(!isCurrentUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                Review aux = reviewMapper.convertReviewDtoToEntity(ev);
                aux = reviewService.addReview(eventId, aux, userId);
                reviewDto = reviewMapper.convertReviewToDto(aux);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            userTrophyService.firstReview(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewDto);
        }
    }

    @DeleteMapping("/users/{userId}/reviews/{reviewId}")
    public ResponseEntity<String> getEventById(@PathVariable Long userId, @PathVariable Long reviewId) {
        if(!isCurrentUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            try {
                reviewService.deleteReview(reviewId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    //Dto conversion functions

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        return userDto;
    }

    private LoggedUserDto convertUserToLoggedUserDto(User user) {
        LoggedUserDto userDto = modelMapper.map(user, LoggedUserDto.class);
        userDto.setPassword(null);
        for (Event e : user.getFavourites()) {
            userDto.addFavourite(e.getId());
        }
        for (Event e : user.getAttendance()) {
            userDto.addAttendance(e.getId());
        }
        for (User u : user.getFriends()) {
            userDto.addFriend(u.getId());
        }
        for (Trophy t : user.getTrophies()) {
            userDto.addTrophy(t.getId());
        }

        return userDto;
    }

    private User convertUserDtoToEntity(UserDto userDto) {
        if (userDto.getRole() == Role.ORGANIZER) {
            return  modelMapper.map(userDto, Organizer.class);
        }
        else return modelMapper.map(userDto, User.class);
    }

    //Utils
    private boolean isCurrentUser(Long userId) {
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User u = userService.getUserByUsername(currentUser.getUsername());
        return u.getId()==userId;
    }

}
