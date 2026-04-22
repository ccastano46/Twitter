package eci.arem.twitter.controller;


import eci.arem.twitter.exception.TwitterException;
import eci.arem.twitter.model.Post;
import eci.arem.twitter.model.User;
import eci.arem.twitter.service.PostService;
import eci.arem.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    /**
     * Creates a new post with optional image attachments.
     *
     * @param content the textual content of the post
     * @param images  the optional list of image files to attach (max 3)
     * @param auth0Id the Auth0 ID of the user creating the post
     * @return a {@link ResponseEntity} containing the created {@link Post} with HTTP 200 OK
     * @throws TwitterException if more than 3 images are provided or the user is invalid
     * @throws IOException      if an error occurs during image upload
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam("content") String content,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestParam String auth0Id) throws TwitterException, IOException {

        User user = userService.findByAuth0Id(auth0Id);
        Post post = postService.createPost(content, images, user);
        return ResponseEntity.ok(post);
    }


    /**
     * Retrieves the global stream of all posts with presigned image URLs.
     *
     * @return a {@link ResponseEntity} containing the list of all {@link Post} entities with HTTP 200 OK
     */
    @GetMapping("/stream")
    public ResponseEntity<List<Post>> getStream() {
        List<Post> posts = postService.getStream();
        posts.forEach(post ->
                post.getImages().forEach(postService::setPostImageUrl)
        );
        return ResponseEntity.ok(posts);
    }


    /**
     * Retrieves all posts created by a specific user, enriched with presigned image URLs.
     *
     * @param auth0Id the Auth0 ID of the user whose posts are to be retrieved
     * @return a {@link ResponseEntity} containing the list of {@link Post} entities with HTTP 200 OK
     * @throws TwitterException if no user is found with the provided Auth0 ID
     */
    @GetMapping("/user")
    public ResponseEntity<List<Post>> getPostsByUser(@RequestParam String auth0Id) throws TwitterException {
        User user = userService.findByAuth0Id(auth0Id);
        List<Post> posts = postService.streamByUser(user);
        posts.forEach(post ->
                post.getImages().forEach(image ->
                        image.setPresignedUrl(postService.generatePresignedUrl(image.getImageKey()))
                )
        );
        return ResponseEntity.ok(posts);
    }

}
