package org.myblog.service;

import org.myblog.model.Article;
import org.myblog.model.Like;
import org.myblog.model.User;
import org.myblog.repository.ArticleRepository;
import org.myblog.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public boolean toggleLike(int articleId, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Статья не найдена"));
        return likeRepository.findByUserAndArticle(user, article)
                .map(existingLike -> {
                    likeRepository.delete(existingLike);
                    return false;
                })
                .orElseGet(() -> {
                    Like like = Like.builder()
                            .user(user)
                            .article(article)
                            .build();
                    likeRepository.save(like);
                    return true;
                });
    }
    public long getLikeCount(int articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article don't exists"));
        return likeRepository.countByArticle(article);
    }
}
