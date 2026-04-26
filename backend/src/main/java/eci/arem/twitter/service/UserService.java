package eci.arem.twitter.service;

import eci.arem.twitter.exception.TwitterException;
import eci.arem.twitter.model.User;
import eci.arem.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by their Auth0 identifier.
     *
     * @param auth0Id the unique Auth0 ID of the user
     * @return the {@link User} entity associated with the given Auth0 ID
     * @throws TwitterException if no user is found with the provided Auth0 ID
     */
    public User findByAuth0Id(String auth0Id) throws TwitterException{
        if(userRepository.findByAuth0Id(auth0Id) == null) throw new TwitterException(TwitterException.USER_NOT_FOUND);
        return userRepository.findByAuth0Id(auth0Id);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user
     * @return the {@link User} entity associated with the given email
     * @throws TwitterException if no user is found with the provided email
     */
    public User findByEmail(String email) throws TwitterException{
        if(userRepository.findByEmail(email) == null) throw new TwitterException(TwitterException.USER_NOT_FOUND);
        return userRepository.findByEmail(email);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username (handle) of the user
     * @return the {@link User} entity associated with the given username
     * @throws TwitterException if no user is found with the provided username
     */
    public User findByUsername(String username) throws TwitterException{
        if(userRepository.findByUsername(username) == null) throw new TwitterException(TwitterException.USER_NOT_FOUND);
        return userRepository.findByUsername(username);
    }

    /**
     * Registers a new user in the system.
     * Validates that the email and username are not already registered.
     *
     * @param user the {@link User} entity to register
     * @return the persisted {@link User} entity
     * @throws TwitterException if the email is already registered
     * @throws TwitterException if the username is already registered
     */
    public User registerUser(User user) throws TwitterException{
        if(userRepository.findByEmail(user.getEmail()) != null)
            throw new TwitterException(TwitterException.EMAIL_REGISTERED);
        if(userRepository.findByUsername(user.getUsername()) != null)
            throw new TwitterException(TwitterException.USERNAME_REGISTERED);
        return userRepository.save(user);
    }

    


}
