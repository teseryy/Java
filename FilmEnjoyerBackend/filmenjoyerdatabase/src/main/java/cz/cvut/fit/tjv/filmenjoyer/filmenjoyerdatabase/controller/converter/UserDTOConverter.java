package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.ReviewServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.UserDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDTOConverter implements DTOConverter<UserDTO, User> {
    private final ReviewServiceImpl reviewService;

    @Override
    public UserDTO toDTO(User e) {
        List<Long> reviewIds = new ArrayList<>();

        for (Review review : e.getReviews()) {
            reviewIds.add(review.getId());
        }

        return new UserDTO(e.getUsername(), e.getName(), e.getSurname(), reviewIds);
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        List<Review> reviews = new ArrayList<>();

        Optional<List<Long>> reviewsUser = Optional.ofNullable(userDTO.getReviewIds());

        if (reviewsUser.isPresent()) {
            for (Long reviewId : reviewsUser.get()) {
                Optional<Review> optionalReview = reviewService.readById(reviewId);
                optionalReview.ifPresent(reviews::add);
            }
        }

        return new User(userDTO.getUsername(), userDTO.getName(), userDTO.getSurname(), reviews);
    }
}
