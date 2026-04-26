package eci.arem.twitter.exception;

public class TwitterException extends Exception{
    public static final String EMAIL_REGISTERED = "Someone already registered with this email";
    public static final String USERNAME_REGISTERED = "Someone already registered with this username";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String THREE_IMAGES= "Maximum 3 images per post";
    public TwitterException(String message){
        super(message);
    }
}