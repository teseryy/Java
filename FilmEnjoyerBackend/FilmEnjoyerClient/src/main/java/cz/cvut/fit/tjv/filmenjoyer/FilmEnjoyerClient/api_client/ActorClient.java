package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.api_client;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ActorDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.FilmDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ActorClient {
    private RestClient actorClient;
    private String baseUrl;
    private RestClient currentActorClient;

    public ActorClient(@Value("${api.url}") String baseUrl){
        this.baseUrl = baseUrl;
        actorClient = RestClient.create(baseUrl + "/actor");
    }

    public void setCurrentActor(Long id){
        currentActorClient = RestClient.builder()
                .baseUrl(baseUrl + "/actor/{id}")
                .defaultUriVariables(Map.of("id", id))
                .build();
    }

    public List<ActorDTO> readAll(){
        return Arrays.asList(actorClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ActorDTO[].class)
                .getBody());
    }

    public Optional<ActorDTO> read(){
        try {
            return Optional.of(currentActorClient.get()
                    .retrieve().toEntity(ActorDTO.class).getBody());
        }catch (HttpClientErrorException.NotFound e){ //RestClientResponseException
            return Optional.empty();
        }
    }

    public void create (ActorDTO actorDTO){
        actorClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(actorDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void delete(){
        ActorDTO actorDTO = currentActorClient.get().retrieve().toEntity(ActorDTO.class).getBody();

        for (Long filmId : actorDTO.getFilmIDs()){
            this.deleteFilmFromActor(filmId);
        }

        currentActorClient.delete()
                .retrieve()
                .toBodilessEntity();
    }

    public void update(ActorDTO actorDTO){
        currentActorClient.put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(actorDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void addFilmToActor(Long filmId){
        Long actorId = currentActorClient.get().retrieve().toEntity(ActorDTO.class).getBody().getId();

        currentActorClient = RestClient.builder()
                .baseUrl(baseUrl + "/actor/{id}/films?filmId={filmId}")
                .defaultUriVariables(Map.of("id", actorId, "filmId", filmId))
                .build();

        currentActorClient.post()
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteFilmFromActor(Long filmId){
        Long actorId = currentActorClient.get().retrieve().toEntity(ActorDTO.class).getBody().getId();

        currentActorClient = RestClient.builder()
                .baseUrl(baseUrl + "/actor/{id}/films/{filmId}")
                .defaultUriVariables(Map.of("id", actorId, "filmId", filmId))
                .build();

        currentActorClient.delete()
                .retrieve()
                .toBodilessEntity();
    }

    public List<FilmDTO> getActorFilms(){
        Long actorId = currentActorClient.get().retrieve().toEntity(ActorDTO.class).getBody().getId();

        currentActorClient = RestClient.builder()
                .baseUrl(baseUrl + "/actor/{id}/films")
                .defaultUriVariables(Map.of("id", actorId))
                .build();

        return Arrays.asList(currentActorClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(FilmDTO[].class)
                .getBody());
    }
}
