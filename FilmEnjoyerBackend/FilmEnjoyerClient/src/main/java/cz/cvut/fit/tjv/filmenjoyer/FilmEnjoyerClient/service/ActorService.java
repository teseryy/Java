package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.api_client.ActorClient;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ActorDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.FilmDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ActorService   {
    private ActorClient actorClient;
    private Long currentActor;

    ActorService(ActorClient actorClient){
        this.actorClient = actorClient;
    }

    public void setCurrentActor(Long actorId){
        currentActor = actorId;
        actorClient.setCurrentActor(actorId);
    }

    public List<ActorDTO> readAll(){
        return actorClient.readAll();
    }

    public Optional<ActorDTO> read(){
        return actorClient.read();
    }

    public void create(ActorDTO data){
        actorClient.create(data);
    }

    public void delete(){
        actorClient.delete();
    }

    public void update(ActorDTO data){
        actorClient.update(data);
    }

    public void addFilmToActor(Long filmId){
        actorClient.addFilmToActor(filmId);
    }

    public void deleteFilmFromActor(Long filmId){
        actorClient.deleteFilmFromActor(filmId);
    }

    public List<FilmDTO> getActorFilms(){
        return actorClient.getActorFilms();
    }
}
