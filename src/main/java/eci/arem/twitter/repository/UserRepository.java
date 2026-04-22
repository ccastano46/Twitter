package eci.arem.twitter.repository;

import eci.arem.twitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByAuth0Id(String auth0Id);
    User findByEmail(String email);
    User findByUsername(String username);

}
