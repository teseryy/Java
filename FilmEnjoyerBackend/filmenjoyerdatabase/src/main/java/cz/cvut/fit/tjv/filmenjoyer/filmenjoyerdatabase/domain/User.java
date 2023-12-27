package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements EntityWithId<String>{
    @Id
    private String username;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Surname is mandatory")
    private String surname;

    @OneToMany(targetEntity = Review.class, mappedBy = "author", fetch = FetchType.LAZY)
    private List<Review> reviews;

    public User(){}

    public User(String username, String name, String surname, List<Review> reviews){
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.reviews = reviews;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof User u){
            return username == null? username == u.username : username.equals(u.username);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return username == null ? 0 : username.hashCode();
    }

    @Override
    public String getId() {
        return username;
    }
}
