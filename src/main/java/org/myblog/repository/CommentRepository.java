package org.myblog.repository;

import org.myblog.model.Article;
import org.myblog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByArticleOrderByCreatedAtAsc(Article article);
}
