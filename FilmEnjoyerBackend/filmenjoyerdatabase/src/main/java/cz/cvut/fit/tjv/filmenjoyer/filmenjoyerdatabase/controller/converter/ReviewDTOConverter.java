package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.FilmServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.UserServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.ReviewDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ReviewDTOConverter implements DTOConverter<ReviewDTO, Review> {
    private final UserServiceImpl userService;
    private final FilmServiceImpl filmService;

    @Override
    public ReviewDTO toDTO(Review e) {
        Optional<Film> optFilm = Optional.ofNullable(e.getFilm());
        Optional<User> optionalUser = Optional.ofNullable(e.getAuthor());

        if (optFilm.isEmpty() && optionalUser.isEmpty()){
            return new ReviewDTO(e.getId(), e.getReviewText(), e.getRating(), e.getDate(), null, null);
        }
        else if (optFilm.isEmpty() && optionalUser.isPresent()){
            return new ReviewDTO(e.getId(), e.getReviewText(), e.getRating(), e.getDate(), null, optionalUser.get().getId());
        }
        else if (optFilm.isPresent() && optionalUser.isEmpty()){
            return new ReviewDTO(e.getId(), e.getReviewText(), e.getRating(), e.getDate(), optFilm.get().getId(), null);
        }

        return new ReviewDTO(e.getId(), e.getReviewText(), e.getRating(), e.getDate(), optFilm.get().getId(), optionalUser.get().getId());
    }

    @Override
    public Review toEntity(ReviewDTO reviewDTO) {
        System.out.println("reviewDTO = " + reviewDTO.getRating());

        Optional<Long> optFilmId = Optional.ofNullable(reviewDTO.getFilmId());
        Optional<String> optUserId = Optional.ofNullable(reviewDTO.getAuthor());

        Optional<Film> optionalFilm = Optional.empty();
        Optional<User> optionalUser = Optional.empty();

        if (optFilmId.isPresent()) {
            optionalFilm = filmService.readById(optFilmId.get());
        }

        if (optUserId.isPresent()) {
            optionalUser = userService.readById(optUserId.get());
        }

        if (optionalFilm.isEmpty() || optionalUser.isEmpty()){
            return new Review(reviewDTO.getId(), reviewDTO.getReviewText(), reviewDTO.getRating(), reviewDTO.getDate());
        }

        return new Review(reviewDTO.getId(), reviewDTO.getReviewText(), reviewDTO.getRating(), reviewDTO.getDate(), optionalFilm.get(), optionalUser.get());

    }
}
