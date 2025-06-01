package org.myblog.controller;

import lombok.RequiredArgsConstructor;
import org.myblog.model.User;
import org.myblog.repository.UserRepository;
import org.myblog.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{articleId}/like")
public class LikeController {
    @Autowired
    private final LikeService likeService;
    @Autowired
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> toggleLike(@PathVariable int articleId, Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean liked = likeService.toggleLike(articleId, user);
        long total = likeService.getLikeCount(articleId);
        return ResponseEntity.ok(Map.of(
                "likesCount", total,
                "liked", liked
        ));
    }
}
