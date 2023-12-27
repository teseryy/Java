package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.UserServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter.DTOConverter;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.ReviewDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.UserDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {
    private final DTOConverter<UserDTO, User> userDTOConverter;
    private final UserServiceImpl userService;
    private final DTOConverter<ReviewDTO, Review> reviewDTOConverter;

    @GetMapping
    public List<UserDTO> getUsers(){
        Iterable<User> users = userService.readAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users){
            userDTOs.add(userDTOConverter.toDTO(user));
        }

        return userDTOs;
    }

    @GetMapping(path = "/{username}")
    public UserDTO getUser(@PathVariable("username") String username){
        return userDTOConverter.toDTO(userService.readById(username).get());
    }

    @GetMapping(path = "/{username}/reviews")
    public List<ReviewDTO> getUserReviews(@PathVariable("username") String username){
        return userService.readById(username).get().getReviews().stream().map(reviewDTOConverter::toDTO).toList();
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO){
        return userDTOConverter.toDTO(userService.create(userDTOConverter.toEntity(userDTO)));
    }

    @PostMapping(path = "/{username}/reviews")
    public List<ReviewDTO> addReviewToUser(@PathVariable("username") String username, Long reviewId){
        userService.addReviewToUser(username, reviewId);

        return userService.readById(username).get().getReviews().stream().map(reviewDTOConverter::toDTO).toList();
    }

    @DeleteMapping(path = "/{username}/reviews/{reviewId}")
    public List<ReviewDTO> removeReviewFromUser(@PathVariable("username") String username, @PathVariable("reviewId") Long reviewId){
        userService.removeReviewFromUser(username, reviewId);

        return userService.readById(username).get().getReviews().stream().map(reviewDTOConverter::toDTO).toList();
    }

    @PutMapping(path = "/{username}")
    public void updateUser(@PathVariable("username") String username, @RequestBody UserDTO userDTO){
        User user = userDTOConverter.toEntity(userDTO);
        user.setUsername(username);
        userService.update(username, user);
    }

    @DeleteMapping(path = "/{username}")
    public void deleteUser(@PathVariable("username") String username){
        userService.deleteById(username);
    }
}
