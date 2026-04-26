package eci.arem.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "USERS")
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User {

    @Id
    @Getter
    @Column(name = "AUTH0_ID", unique = true, nullable = false)
    private String auth0Id;


    @Column(name = "EMAIL",unique = true, nullable = false)
    private String email;


    @Column(name = "USERNAME",unique = true, nullable = false)
    private String username;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Post> posts;

    /**
     * Constructs a new User.
     *
     * @param auth0Id  the unique Auth0 identifier for the user
     * @param email    the user's email address
     * @param username the user's chosen username
     */
    public User(String auth0Id, String email, String username) {
        this.auth0Id = auth0Id;
        this.email = email;
        this.username = username;
        this.createdAt = LocalDateTime.now();
        this.posts = new ArrayList<>();
    }
}