package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.ActorServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.ReviewServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.FilmDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Actor;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class FilmDTOConverter implements DTOConverter<FilmDTO, Film> {
    private final ReviewServiceImpl reviewService;
    private final ActorServiceImpl actorService;

    @Override
    public FilmDTO toDTO(Film e) {
        List<Long> actorIds = new ArrayList<>();
        List<Long> reviewIds = new ArrayList<>();

        for (Actor actor : e.getActors()) {
            actorIds.add(actor.getId());
        }

        for (Review review : e.getReviews()) {
            reviewIds.add(review.getId());
        }

        return new FilmDTO(e.getId(), e.getTitle(), e.getDuration(), e.getDirector(), e.getDateOfRelease(), e.getBudget(), e.getCountry(), reviewIds, actorIds);
    }

    @Override
    public Film toEntity(FilmDTO filmDTO) {
        List<Review> reviews = new ArrayList<>();
        List<Actor> actors = new ArrayList<>();

        Optional<List<Long>> actorsFilm = Optional.ofNullable(filmDTO.getActorIds());
        Optional<List<Long>> reviewsFilm = Optional.ofNullable(filmDTO.getReviewIds());

        if (actorsFilm.isPresent()) {
            for (Long reviewId : actorsFilm.get()) {
                Optional<Review> optionalReview = reviewService.readById(reviewId);
                optionalReview.ifPresent(reviews::add);
            }
        }

        if (reviewsFilm.isPresent()) {
            for (Long actorId : reviewsFilm.get()) {
                Optional<Actor> optionalActor = actorService.readById(actorId);
                optionalActor.ifPresent(actors::add);
            }
        }

        return new Film(filmDTO.getId(), filmDTO.getTitle(), filmDTO.getDuration(), filmDTO.getDirector(),
                        filmDTO.getDateOfRelease(), filmDTO.getBudget(), filmDTO.getCountry(), reviews, actors);
    }
}
