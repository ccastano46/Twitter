package eci.arem.twitter.service;

import eci.arem.twitter.exception.TwitterException;
import eci.arem.twitter.model.Post;
import eci.arem.twitter.model.PostImage;
import eci.arem.twitter.model.User;
import eci.arem.twitter.repository.PostImageRepository;
import eci.arem.twitter.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {
    private PostRepository postRepository;
    private PostImageRepository postImageRepository;
    private final S3Client s3Client;
    private final S3Presigner presigner;
    private final String bucketName;

    @Autowired
    public PostService(
            PostRepository postRepository,
            PostImageRepository postImageRepository,
            @Value("${AWS_REGION}") String region,
            @Value("${AWS_BUCKET}") String bucketName) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        Region awsRegion = Region.of(region);
        this.s3Client = S3Client.builder().region(awsRegion).build();
        this.presigner = S3Presigner.builder().region(awsRegion).build();
        this.bucketName = bucketName;
    }

    /**
     * Uploads an image file to the configured S3 bucket.
     *
     * @param file the {@link MultipartFile} to upload
     * @return the S3 object key (file name) assigned to the uploaded image
     * @throws IOException if an I/O error occurs during the upload process
     */
    private String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );
        return fileName;
    }

    /**
     * Generates a temporary presigned URL for accessing an image stored in S3.
     * The URL is valid for 15 minutes.
     *
     * @param fileName the S3 object key of the image
     * @return a presigned URL string granting temporary read access to the image
     */
    public String generatePresignedUrl(String fileName) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(req -> req.bucket(bucketName).key(fileName))
                .build();
        return presigner.presignGetObject(presignRequest).url().toString();
    }

    /**
     * Creates a new post with optional image attachments.
     * Images are uploaded to S3 and associated with the post.
     *
     * @param content the textual content of the post
     * @param images  the list of image files to attach; may be null or empty
     * @param user    the author of the post
     * @return the persisted {@link Post} entity with associated images
     * @throws TwitterException if more than 3 images are provided
     * @throws IOException      if an error occurs during image upload to S3
     */
    public Post createPost(String content, List<MultipartFile> images, User user) throws TwitterException, IOException {
        if (images != null && images.size() > 3) {
            throw new TwitterException(TwitterException.THREE_IMAGES);
        }

        Post post = new Post(content, user);
        postRepository.save(post); // Primero guarda el post para que tenga ID

        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                String imageKey = uploadImage(images.get(i));
                PostImage postImage = new PostImage(imageKey, i + 1, post);
                post.addImage(postImage);
            }
            postImageRepository.saveAll(post.getImages());
        }

        return postRepository.save(post); // Actualiza con las imágenes
    }

    /**
     * Retrieves the global stream of all posts ordered by creation date in descending order.
     *
     * @return a list of all {@link Post} entities, newest first
     */
    public List<Post> getStream() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Retrieves all posts created by a specific user, ordered by creation date in descending order.
     *
     * @param user the {@link User} whose posts are to be retrieved
     * @return a list of {@link Post} entities authored by the given user, newest first
     */
    public List<Post> streamByUser(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Sets a temporary presigned URL on a {@link PostImage} for client access.
     *
     * @param image the {@link PostImage} to enrich with a presigned URL
     */
    public void setPostImageUrl(PostImage image) {
        image.setPresignedUrl(generatePresignedUrl(image.getImageKey()));
    }
}