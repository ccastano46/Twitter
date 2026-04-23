package eci.arem.twitter.model;

import eci.arem.twitter.exception.TwitterException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "POSTS")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Size(max = 140)
    @Column(name = "CONTENT",nullable = false, length = 140)
    private String content;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Size(min = 1, max = 3)
    private List<PostImage> images;

    /**
     * Constructs a new Post with the specified content and author.
     *
     * @param content the textual content of the post; must not be blank and max 140 chars
     * @param user    the author of the post; must not be null
     */
    public Post(String content, User user) {
        this.content = content;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.images = new ArrayList<>();
    }

    /**
     * Adds an image to this post.
     *
     * @param image the {@link PostImage} to attach to this post
     * @throws TwitterException if the post already contains the maximum of 3 images
     */
    public void addImage(PostImage image) throws TwitterException {
        if (this.images.size() >= 3) throw new TwitterException(TwitterException.THREE_IMAGES);
        this.images.add(image);
    }

}