package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;


import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.User;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.ReviewRepository;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl extends CrudServiceImpl<User, String> implements UserServiceInterface {
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;

    @Override
    public void addReviewToUser(String username, Long reviewId) throws IllegalArgumentException {
        Optional<User> optUser = userRepository.findById(username);
        Optional<Review> optReview = reviewRepository.findById(reviewId);
        if (optUser.isEmpty()){
            throw new IllegalArgumentException("There's no such user with that username.");
        }

        if (optReview.isEmpty()){
            throw new IllegalArgumentException("There's no such film with that id.");
        }

        User user = optUser.get();
        Review review = optReview.get();

        user.getReviews().add(review);
        review.setAuthor(user);

        userRepository.save(user);
        reviewRepository.save(review);
    }

    @Override
    public void removeReviewFromUser(String username, Long reviewId) throws IllegalArgumentException {
        Optional<User> optUser = userRepository.findById(username);
        Optional<Review> optReview = reviewRepository.findById(reviewId);
        if (optUser.isEmpty()){
            throw new IllegalArgumentException("There's no such user with that username.");
        }

        if (optReview.isEmpty()){
            throw new IllegalArgumentException("There's no such film with that id.");
        }

        User user = optUser.get();
        Review review = optReview.get();

        if (!review.getAuthor().equals(user)){
            throw new IllegalArgumentException("The user: " + username + " does not have a review with this idi");
        }

        user.getReviews().remove(review);
        review.setAuthor(null);

        userRepository.save(user);
        reviewRepository.save(review);
    }

    @Override
    protected CrudRepository<User, String> getRepository() {
        return userRepository;
    }
}
