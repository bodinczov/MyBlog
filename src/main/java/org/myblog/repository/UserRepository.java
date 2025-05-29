package org.myblog.repository;

import org.myblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<String, Integer> {
    Optional<User> findByUsername(String username);
}
