package org.myblog.service;

import org.myblog.model.Article;
import org.myblog.model.Comment;
import org.myblog.model.User;
import org.myblog.repository.ArticleRepository;
import org.myblog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public List<Comment> getCommentsByArticle(int articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + articleId));

        return commentRepository.findByArticleOrderByCreatedAtAsc(article);
    }
    public Comment addComment(int articleId, String text, User author){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + articleId));
        Comment comment = Comment.builder()
                .text(text)
                .createdAt(LocalDate.now())
                .author(author)
                .article(article)
                .build();
        return commentRepository.save(comment);
    }
    public void deleteComment(int commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));

        boolean isAuthor = (comment.getAuthor().getId() == (user.getId()));
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAuthor && !isAdmin) {
            throw new SecurityException("Access denied: you are not the author or an admin");
        }

        commentRepository.delete(comment);
    }
}
