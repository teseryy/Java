package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.persistent;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
