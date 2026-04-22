package eci.arem.twitter.repository;

import eci.arem.twitter.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, String> {
    List<PostImage> findByPostIdOrderByDisplayOrderAsc(String postId);
    int countByPostId(String postId);
}