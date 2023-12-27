package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.ReviewServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter.DTOConverter;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.FilmDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.ReviewDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.UserDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/api/review", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ReviewController {
    private final DTOConverter<ReviewDTO, Review> reviewDTOConverter;
    private final ReviewServiceImpl reviewService;
    private final DTOConverter<FilmDTO, Film> filmDTOConverter;
    private final DTOConverter<UserDTO, User> userDTOConverter;

    @GetMapping
    public List<ReviewDTO> getReviews(){
        Iterable<Review> reviews = reviewService.readAll();
        List<ReviewDTO> reviewDTOs = new ArrayList<>();

        for (Review review : reviews){
            reviewDTOs.add(reviewDTOConverter.toDTO(review));
        }

        return reviewDTOs;
    }

    @GetMapping(path = "/{id}")
    public ReviewDTO getReview(@PathVariable("id") Long id){
        return reviewDTOConverter.toDTO(reviewService.readById(id).get());
    }

    @GetMapping(path = "/{id}/film")
    public FilmDTO getReviewFilm(@PathVariable("id") Long id){
        return filmDTOConverter.toDTO(reviewService.readById(id).get().getFilm());
    }

    @GetMapping(path = "/{id}/author")
    public UserDTO getReviewAuthor(@PathVariable("id") Long id){
        return userDTOConverter.toDTO(reviewService.readById(id).get().getAuthor());
    }

    @PostMapping
    public ReviewDTO createReview(@RequestBody ReviewDTO reviewDTO){
        return reviewDTOConverter.toDTO(reviewService.create(reviewDTOConverter.toEntity(reviewDTO)));
    }

    @PutMapping(path = "/{id}")
    public void updateReview(@PathVariable("id") Long id, ReviewDTO reviewDTO){
        Review review = reviewDTOConverter.toEntity(reviewDTO);
        review.setId(id);
        reviewService.update(id, review);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteReview(@PathVariable("id") Long id){
        reviewService.deleteById(id);
    }
}
