package eci.arem.twitter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "POST_IMAGE")
@Getter
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "IMAGE_KEY", nullable = false)
    private String imageKey;

    @Column(name = "DISPLAY_ORDER",nullable = false)
    private int displayOrder;

    @Transient
    @Setter
    private String presignedUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post", nullable = false)
    private Post post;

    /**
     * Constructs a new PostImage.
     *
     * @param imageKey      the S3 object key for the image
     * @param displayOrder  the display order of the image within the post
     * @param post          the parent {@link Post} this image belongs to
     */
    public PostImage(String imageKey, int displayOrder, Post post) {
        this.imageKey = imageKey;
        this.displayOrder = displayOrder;
        this.post = post;
    }
}