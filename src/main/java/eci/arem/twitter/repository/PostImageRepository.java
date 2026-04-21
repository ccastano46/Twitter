package eci.arem.twitter.repository;

import eci.arem.twitter.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, String> {
    List<PostImage> findByPostIdOrderByDisplayOrderAsc(String postId);
    int countByPostId(String postId);
}