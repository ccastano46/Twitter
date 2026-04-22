package eci.arem.twitter.controller;


import eci.arem.twitter.exception.TwitterException;
import eci.arem.twitter.model.User;
import eci.arem.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Registers a new user in the system.
     *
     * @param user the {@link User} entity containing registration details
     * @return a {@link ResponseEntity} containing the created {@link User} with HTTP 201 Created
     * @throws TwitterException if the email or username is already registered
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws TwitterException {
        return ResponseEntity.status(201).body(userService.registerUser(user));
    }


    /**
     * Retrieves the authenticated user's profile by their Auth0 ID.
     *
     * @param auth0Id the Auth0 ID of the user
     * @return a {@link ResponseEntity} containing the {@link User} entity with HTTP 200 OK
     * @throws TwitterException if no user is found with the provided Auth0 ID
     */
    @GetMapping("/me")
    public ResponseEntity<User> getMe(@RequestParam String auth0Id) throws TwitterException {
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
