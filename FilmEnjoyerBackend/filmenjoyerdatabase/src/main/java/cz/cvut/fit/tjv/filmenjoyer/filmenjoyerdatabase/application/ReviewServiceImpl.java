package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class ReviewServiceImpl extends CrudServiceImpl<Review, Long> implements ReviewServiceInterface{
    private ReviewRepository reviewRepository;

    @Override
    protected CrudRepository<Review, Long> getRepository() {
        return reviewRepository;
    }
}
