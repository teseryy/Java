package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import java.util.Optional;

public interface CrudService<T, ID> {
    T create(T e) throws IllegalArgumentException;
    Optional<T> readById(ID id);
    Iterable<T> readAll();
    void update(ID id, T e) throws IllegalArgumentException;
    void deleteById(ID id) throws IllegalArgumentException;
}
