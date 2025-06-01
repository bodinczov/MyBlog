package org.myblog.service;

import lombok.RequiredArgsConstructor;
import org.myblog.model.Article;
import org.myblog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.myblog.model.User;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    ArticleRepository articleRepository;

    public Article createArticle(String title, String body, User author) {
        Article article = Article.builder()
                .title(title)
                .body(body)
                .createdAt(LocalDateTime.now())
                .author(author)
                .build();

        return articleRepository.save(article);
    }
    public Article updateArticle(int id, String title, String body) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with id: " + id));

        article.setTitle(title);
        article.setBody(body);
        return articleRepository.save(article);
    }
    public void deleteArticle(int id) {
        if (!articleRepository.existsById(id)) {
            throw new IllegalArgumentException("Article not found with id: " + id);
        }
        articleRepository.deleteById(id);
    }
}
