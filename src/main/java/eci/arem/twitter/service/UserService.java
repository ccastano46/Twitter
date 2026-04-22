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

    public User findByAuth0Id(String auth0Id) throws TwitterException{
        if(userRepository.findByAuth0Id(auth0Id) == null) throw new TwitterException(TwitterException.USER_NOT_FOUND);
        return userRepository.findByAuth0Id(auth0Id);
    }

    public User findByEmail(String email) throws TwitterException{
        if(userRepository.findByEmail(email) == null) throw new TwitterException(TwitterException.USER_NOT_FOUND);
        return userRepository.findByEmail(email);
    }

    public User findByUsername(String username) throws TwitterException{
        if(userRepository.findByUsername(username) == null) throw new TwitterException(TwitterException.USER_NOT_FOUND);
        return userRepository.findByUsername(username);
    }

    public User registerUser(User user) throws TwitterException{
        if(userRepository.findByEmail(user.getEmail()) != null)
            throw new TwitterException(TwitterException.EMAIL_REGISTERED);
        if(userRepository.findByUsername(user.getUsername()) != null)
            throw new TwitterException(TwitterException.USERNAME_REGISTERED);
        return userRepository.save(user);
    }

    


}
