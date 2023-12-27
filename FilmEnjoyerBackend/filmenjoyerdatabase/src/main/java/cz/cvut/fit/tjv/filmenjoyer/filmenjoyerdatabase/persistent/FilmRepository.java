package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends CrudRepository<Film, Long> {
    @Query(value = "select f from Film f join f.reviews r where r.rating >= 8")
    List<Film> findFilmsWithReviewRating8AndMore();
}
