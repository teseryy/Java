package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
}
