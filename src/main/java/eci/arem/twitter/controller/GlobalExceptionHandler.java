package eci.arem.twitter.controller;

import eci.arem.twitter.exception.TwitterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link TwitterException} thrown by controllers or services.
     * Maps specific error messages to their corresponding HTTP status codes:
     * <ul>
     *   <li>404 Not Found for {@code USER_NOT_FOUND}</li>
     *   <li>409 Conflict for {@code EMAIL_REGISTERED} or {@code USERNAME_REGISTERED}</li>
     *   <li>400 Bad Request for all other Twitter exceptions</li>
     * </ul>
     *
     * @param e the {@link TwitterException} to handle
     * @return a {@link ResponseEntity} containing the exception message and the mapped HTTP status
     */
    @ExceptionHandler(TwitterException.class)
    public ResponseEntity<String> handleTwitterException(TwitterException e) {
        if (e.getMessage().equals(TwitterException.USER_NOT_FOUND)) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        if (e.getMessage().equals(TwitterException.EMAIL_REGISTERED) ||
                e.getMessage().equals(TwitterException.USERNAME_REGISTERED)) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
