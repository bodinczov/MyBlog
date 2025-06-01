package org.myblog.repository;

import org.myblog.model.Article;
import org.myblog.model.Like;
import org.myblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndArticle(User user, Article article);
    long countByArticle(Article article);
}
