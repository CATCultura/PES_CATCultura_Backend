package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.*;
import cat.cultura.backend.entity.*;
import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.mappers.ReviewMapper;
import cat.cultura.backend.mappers.RouteMapper;
import cat.cultura.backend.mappers.TrophyMapper;
import cat.cultura.backend.service.*;
import cat.cultura.backend.service.user.*;
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
    private UserRouteService userRouteService;

    @Autowired
    private RouteDataService routeDataService;

    @Autowired
    private UserTagService userTagService;

    @Autowired
    private AttendedService attendedService;

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
        User createdUser = userService.createUser(userEntity);
        userTrophyService.createAccount(createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertUserToLoggedUserDto(createdUser));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsersByQuery(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "name-and-surname", required = false) String nameAndSurname,
            Pageable pageable
    ) {
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
    public ResponseEntity<List<Long>> addAttendance(@PathVariable Long id, @PathVariable Long eventId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Event> attendance;
            try {
                attendance = attendanceService.addAttendance(id, eventId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            userTrophyService.firstAttendance(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(attendance.stream().map(Event::getId).toList());
        }
    }

    @GetMapping("/users/{id}/attendance")
    public ResponseEntity<List<EventDto>> getAttendance(@PathVariable Long id) {
        List<Event> events = userService.getAttendanceEventsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @DeleteMapping("/users/{id}/attendance/{eventId}")
    public ResponseEntity<List<Long>> removeAttendance(@PathVariable Long id, @PathVariable Long eventId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Event> attendance;
            try {
                attendance = attendanceService.removeAttendance(id, eventId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(attendance.stream().map(Event::getId).toList());
        }
    }

    @PutMapping("/users/{id}/attended/{eventId}")
    public ResponseEntity<List<Long>> confirmAttendance(@PathVariable Long id, @PathVariable Long eventId, @RequestParam String code) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Event> attended;
            try {
                attended = attendedService.confirmAttendance(id, eventId, code);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(attended.stream().map(Event::getId).toList());
        }
    }

    @GetMapping("/users/{id}/attended")
    public ResponseEntity<List<EventDto>> getAttended(@PathVariable Long id) {
        List<Event> events = userService.getAttendedEventsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @DeleteMapping("/users/{id}/attended/{eventId}")
    public ResponseEntity<List<Long>> removeAttended(@PathVariable Long id, @PathVariable Long eventId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Event> attended;
            try {
                attended = attendedService.removeAttended(id, eventId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(attended.stream().map(Event::getId).toList());
        }
    }

    //Put, Get and Delete for favourites of a user

    @PutMapping("/users/{id}/favourites/{eventId}")
    public ResponseEntity<List<Long>> addFavourites(@PathVariable Long id, @PathVariable Long eventId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Event> favourites;
            try {
                favourites = favouriteService.addFavourite(id, eventId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            userTrophyService.firstFavourite(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(favourites.stream().map(Event::getId).toList());
        }
    }

    @GetMapping("/users/{id}/favourites")
    public ResponseEntity<List<EventDto>> getFavourites(@PathVariable Long id) {
        List<Event> events = userService.getFavouriteEventsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @DeleteMapping("/users/{id}/favourites/{eventId}")
    public ResponseEntity<List<Long>> removeFavourites(@PathVariable Long id, @PathVariable Long eventId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Event> favourites;
            try {
                favourites = favouriteService.removeFavourite(id, eventId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(favourites.stream().map(Event::getId).toList());
        }
    }

    //Put, Get and Delete for trophies of a user

    @PutMapping("/users/{id}/trophies")
    public ResponseEntity<List<Long>> addTrophies(@PathVariable Long id, @RequestBody List<Long> trophiesIds) {
        List<Trophy> trophies;
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            try {
                trophies = userTrophyService.addTrophy(id, trophiesIds);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(trophies.stream().map(Trophy::getId).toList());
        }
    }

    @GetMapping("/users/{id}/trophies")
    public ResponseEntity<List<TrophyDto>> getTrophies(@PathVariable Long id) {
        List<Trophy> trophies = userTrophyService.getTrophiesById(id);
        return ResponseEntity.status(HttpStatus.OK).body(trophies.stream().map(trophyMapper::convertTrophyToDto).toList());
    }

    @DeleteMapping("/users/{id}/trophies")
    public ResponseEntity<List<Long>> removeTrophies(@PathVariable Long id, @RequestBody List<Long> trophiesIds) {
        List<Trophy> trophies;
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            try {
                trophies = userTrophyService.removeTrophy(id, trophiesIds);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(trophies.stream().map(Trophy::getId).toList());
        }
    }

    //Put, Get and Delete for the friend requests to other users of a user

    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<List<Long>> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<User> friend;
            try {
                friend = requestService.addFriend(id, friendId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(friend.stream().map(User::getId).toList());
        }
    }

    @GetMapping("users/{id}/friends")
    public ResponseEntity<List<UserDto>> getFriend(@PathVariable Long id, @RequestParam String status) {
        List<User> users = new ArrayList<>();
        if(Objects.equals(status, "requested")) users = requestService.getRequestsTo(id);
        else if(Objects.equals(status, "received")) users = requestService.getRequestFrom(id);
        else if(Objects.equals(status, "accepted")) users = requestService.getFriends(id);
        return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(this::convertUserToDto).toList());
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<List<Long>> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<User> friend;
            try {
                friend = requestService.removeFriend(id, friendId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(friend.stream().map(User::getId).toList());
        }
    }

    @GetMapping("/users/generate_route")
    public ResponseEntity<List<EventDto>> generateRoute(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam int radius,
            @RequestParam String day,
            @RequestParam(required = false) Long userId
    ) {
        List<Event> events;
        if(userId != null) {
            events = routeDataService.getUserAdjustedRouteInADayAndLocation(lat, lon, radius, day, userId);
            userTrophyService.firstRoute(userId);
        } else {
            events = routeDataService.getRouteInADayAndLocation(lat, lon, radius, day);
        }
        return ResponseEntity.status(HttpStatus.OK).body(events.stream().map(eventMapper::convertEventToDto).toList());
    }

    @GetMapping("/users/{id}/routes")
    public ResponseEntity<List<RouteDto>> getRoutes(@PathVariable Long id) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Route> routes = userRouteService.getRoutes(id);
            return ResponseEntity.status(HttpStatus.OK).body(routes.stream().map(routeMapper::convertRouteToDto).toList());
        }
    }

    @PutMapping("/users/{id}/routes")
    public ResponseEntity<List<Long>> saveRoute(@PathVariable Long id, @RequestBody RouteDto routeDto) {
        if(!isCurrentUser(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Route r = routeMapper.convertRouteDtoToEntity(routeDto);
            List<Route> result = userRouteService.saveRoute(r,id);
            return ResponseEntity.status(HttpStatus.CREATED).body(result.stream().map(Route::getRouteId).toList());
        }
    }

    @DeleteMapping("/users/{userId}/routes/{routeId}")
    public ResponseEntity<List<Long>> removeRoute(@PathVariable Long userId, @PathVariable Long routeId) {
        if(!isCurrentUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Route> result;
            try {
                result = userRouteService.deleteRoute(routeId,userId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(result.stream().map(Route::getRouteId).toList());
        }
    }

    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(reviews.stream().map(reviewMapper::convertReviewToDto).toList());
    }

    @PostMapping("/users/{userId}/reviews")
    public ResponseEntity<List<Long>> addReview(
            @PathVariable Long userId,
            @RequestParam Long eventId,
            @RequestBody ReviewDto r) {
        if(!isCurrentUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Review> reviews;
            try {
                reviews = reviewService.addReview(eventId, reviewMapper.convertReviewDtoToEntity(r), userId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            userTrophyService.firstReview(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(reviews.stream().map(Review::getId).toList());
        }
    }

    @DeleteMapping("/users/{userId}/reviews/{reviewId}")
    public ResponseEntity<List<Long>> removeReview(@PathVariable Long userId, @PathVariable Long reviewId) {
        if(!isCurrentUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Review> reviews;
            try {
                reviews = reviewService.deleteReview(reviewId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(reviews.stream().map(Review::getId).toList());
        }
    }


    @GetMapping("/users/{userId}/tags")
    public ResponseEntity<Map<String,List<String>>> getTags(@PathVariable Long userId) {
        List<Tag> tagList = userTagService.getTags(userId);
        Map<String, List<String>> result = new HashMap<>();
        for (Tag t : tagList) {
            if (!result.containsKey(t.getType().toString())){
                result.put(t.getType().toString(),new ArrayList<>());
            }
            result.get(t.getType().toString()).add(t.getValue());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/users/{userId}/tags")
    public ResponseEntity<String> addTags(@PathVariable Long userId, @RequestBody List<String> tags) {
        userTagService.addTags(userId,tags);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/users/{userId}/tags")
    public ResponseEntity<String> removeTags(@PathVariable Long userId, @RequestBody List<String> tags) {
        userTagService.removeTags(userId,tags);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/users/{userId}/upvotes")
    public ResponseEntity<List<ReviewDto>> getUpvotes(@PathVariable Long userId) {
        List<Review> reviews;
        if(!isCurrentUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            try {
                reviews = reviewService.getUpvotes(userId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(reviews.stream().map(reviewMapper::convertReviewToDto).toList());
        }
    }

    @PostMapping("/users/{userId}/upvotes/{reviewId}")
    public ResponseEntity<List<Long>> upvote(@PathVariable Long userId, @PathVariable Long reviewId) {
        if(!isCurrentUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Review> reviews;
            try {
                reviews = reviewService.upvote(userId,reviewId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(reviews.stream().map(Review::getId).toList());
        }
    }

    @DeleteMapping("/users/{userId}/upvotes/{reviewId}")
    public ResponseEntity<List<Long>> deleteUpvotes(@PathVariable Long userId, @PathVariable Long reviewId) {
        if(!isCurrentUser(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<Review> reviews;
            try {
                reviews = reviewService.unvote(userId,reviewId);
            } catch (AssertionError as) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(reviews.stream().map(Review::getId).toList());
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
        return Objects.equals(u.getId(), userId);
    }

}
