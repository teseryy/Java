package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Long> {
}
