package eci.arem.twitter.controller;


import eci.arem.twitter.dto.RegisterRequest;
import eci.arem.twitter.exception.TwitterException;
import eci.arem.twitter.model.User;
import eci.arem.twitter.service.Auth0Service;
import eci.arem.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final Auth0Service auth0Service;

    @Autowired
    public UserController(UserService userService, Auth0Service auth0Service) {
        this.userService = userService;
        this.auth0Service = auth0Service;
    }

    /**
     * Registers a new user in the system.
     * Creates the user in Auth0 and saves them in the database.
     *
     * @param request the registration details (username, email, password)
     * @return a {@link ResponseEntity} containing the created {@link User} with HTTP 201 Created
     * @throws TwitterException if the email or username is already registered
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestBody RegisterRequest request) throws TwitterException {
        try {
            String auth0Id = auth0Service.createUser(request.getEmail(), request.getPassword());
            User user = new User(auth0Id, request.getEmail(), request.getUsername());
            return ResponseEntity.status(201).body(userService.registerUser(user));
        } catch (TwitterException e) {
            throw e;
        } catch (Exception e) {
            throw new TwitterException(e.getMessage());
        }
    }



    /**
     * Retrieves the authenticated user's profile by their Auth0 ID.
     *
     * @param auth0Id the Auth0 ID of the user
     * @return a {@link ResponseEntity} containing the {@link User} entity with HTTP 200 OK
     * @throws TwitterException if no user is found with the provided Auth0 ID
     */
    @GetMapping("/me")
    public ResponseEntity<User> getMe(@AuthenticationPrincipal Jwt jwt) throws TwitterException {
        String auth0Id = jwt.getSubject();
        return ResponseEntity.ok(userService.findByAuth0Id(auth0Id));
    }


    /**
     * Retrieves a user's profile by their username.
     *
     * @param username the unique username of the user
     * @return a {@link ResponseEntity} containing the {@link User} entity with HTTP 200 OK
     * @throws TwitterException if no user is found with the provided username
     */
    @GetMapping("/users/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) throws TwitterException {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

}
