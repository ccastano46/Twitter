package eci.arem.twitter.repository;

import eci.arem.twitter.model.Post;
import eci.arem.twitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    Post findByUser(User user);
    List<Post> findAllByOrderByCreatedAtDesc();
}
