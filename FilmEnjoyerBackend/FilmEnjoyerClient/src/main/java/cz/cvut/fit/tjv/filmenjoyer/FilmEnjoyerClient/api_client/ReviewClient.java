package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.api_client;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ReviewDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Component
public class ReviewClient {
    private RestClient reviewClient;
    private String baseUrl;
    private RestClient currentReviewClient;

    ReviewClient(@Value("${api.url}") String baseUrl){
        this.baseUrl = baseUrl;
        reviewClient = RestClient.create(baseUrl + "/review");
    }

    public List<ReviewDTO> readAll(){
        return Arrays.asList(reviewClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(ReviewDTO[].class)
                .getBody());
    }

    public void setCurrentReview(Long id){
        currentReviewClient = RestClient.builder()
                .baseUrl(baseUrl + "/review/{id}")
                .defaultUriVariables(Map.of("id", id))
                .build();
    }

    public Optional<ReviewDTO> read(){
        try {
            return Optional.of(currentReviewClient.get()
                    .retrieve().toEntity(ReviewDTO.class).getBody());
        }catch (HttpClientErrorException.NotFound e){ //RestClientResponseException
            return Optional.empty();
        }
    }

    public Long create (ReviewDTO reviewDTO){
        reviewDTO.setDate(new Date());
        Optional<ReviewDTO> responseDTO = Optional.ofNullable(reviewClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewDTO)
                .retrieve()
                .toEntity(ReviewDTO.class).getBody());

        if (responseDTO.isPresent()) {
            return responseDTO.get().getId();
        }

        return -1L;
    }

    public void delete(){
        currentReviewClient.delete()
                .retrieve()
                .toBodilessEntity();
    }

    public void update(ReviewDTO reviewDTO){
        reviewDTO.setDate(new Date());
        currentReviewClient.put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewDTO)
                .retrieve()
                .toBodilessEntity();
    }
}
