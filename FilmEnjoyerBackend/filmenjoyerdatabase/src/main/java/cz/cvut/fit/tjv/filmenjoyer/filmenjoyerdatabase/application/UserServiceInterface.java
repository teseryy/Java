package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.User;

public interface UserServiceInterface extends CrudService<User, String> {
    public void addReviewToUser(String username, Long reviewId) throws IllegalArgumentException;

    public void removeReviewFromUser(String username, Long reviewId) throws IllegalArgumentException;
}
