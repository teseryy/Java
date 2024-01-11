package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.api_client.UserClient;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ReviewDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    private UserClient userClient;
    private String currentUser;

    UserService(UserClient userClient){
        this.userClient = userClient;
    }

    public void setCurrentUser(String username){
        currentUser = username;
        userClient.setCurrentUser(username);
    }

    public List<UserDTO> readAll(){
        return userClient.readAll();
    }

    public Optional<UserDTO> read(){
        return userClient.read();
    }

    public void create(UserDTO data){
        userClient.create(data);
    }

    public void delete(){
        userClient.delete();
    }

    public void update(UserDTO data){
        userClient.update(data);
    }

    public List<ReviewDTO> getUserReviews(){
        return userClient.getUserReviews();
    }
}
