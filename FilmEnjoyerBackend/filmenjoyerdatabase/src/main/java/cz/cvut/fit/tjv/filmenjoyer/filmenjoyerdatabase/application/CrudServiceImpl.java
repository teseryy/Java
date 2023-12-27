package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application;

import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.EntityWithId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class CrudServiceImpl<T extends EntityWithId<ID>, ID> implements CrudService<T, ID>{
    @Override
    public T create(T e) throws IllegalArgumentException{
        Optional<ID> id = Optional.ofNullable(e.getId());
        if (id.isPresent()) {
            if (getRepository().existsById(id.get()))
                throw new IllegalArgumentException();
        }

        return getRepository().save(e);
    }

    @Override
    public Optional<T> readById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Iterable<T> readAll() {
        return getRepository().findAll();
    }

    @Override
    public void update(ID id, T e) throws IllegalArgumentException{
        if (!getRepository().existsById(id))
            throw new IllegalArgumentException();

        getRepository().save(e);
    }

    @Override
    public void deleteById(ID id) throws IllegalArgumentException{
        if (getRepository().existsById(id))
            getRepository().deleteById(id);
        else
            throw new IllegalArgumentException();
    }

    protected abstract CrudRepository<T, ID> getRepository();
}
