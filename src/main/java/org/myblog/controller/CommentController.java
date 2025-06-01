package org.myblog.controller;

import lombok.RequiredArgsConstructor;
import org.myblog.model.CommentRequest;
import org.myblog.model.Comment;
import org.myblog.model.User;
import org.myblog.repository.UserRepository;
import org.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable int articleId) {
        List<Comment> comments = commentService.getCommentsByArticle(articleId);

        var response = comments.stream().map(c -> Map.of(
                "author", c.getAuthor().getUsername(),
                "createdAt", c.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                "text", c.getText()
        )).toList();

        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<?> add(
            @PathVariable int articleId,
            @RequestBody CommentRequest request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (request.getText() == null || request.getText().isBlank()) {
            return ResponseEntity.badRequest().body("Комментарий не должен быть пустым");
        }
        Comment saved = commentService.addComment(articleId, request.getText(), user);
        return ResponseEntity.ok(Map.of(
                "author", saved.getAuthor().getUsername(),
                "createdAt", saved.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                "text", saved.getText()
        ));
    }
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> delete(
            @PathVariable int commentId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            commentService.deleteComment(commentId, user);
            return ResponseEntity.ok("Комментарий удалён");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("Недостаточно прав для удаления");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
