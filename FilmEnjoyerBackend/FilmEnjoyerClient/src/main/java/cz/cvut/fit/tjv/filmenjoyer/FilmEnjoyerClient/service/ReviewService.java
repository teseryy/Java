package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.api_client.ReviewClient;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ReviewDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReviewService {
    private ReviewClient reviewClient;
    private Long currentReview;

    ReviewService(ReviewClient reviewClient){
        this.reviewClient = reviewClient;
    }

    public void setCurrentReview(Long reviewId){
        currentReview = reviewId;
        reviewClient.setCurrentReview(reviewId);
    }

    public Optional<ReviewDTO> read(){
        return reviewClient.read();
    }

    public void create(ReviewDTO data){
        reviewClient.create(data);
    }

    public void delete(){
        reviewClient.delete();
    }

    public void update(ReviewDTO data){
        reviewClient.update(data);
    }
}
