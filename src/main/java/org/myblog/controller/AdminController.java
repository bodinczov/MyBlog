package org.myblog.controller;

import org.myblog.model.Article;
import org.myblog.model.User;
import org.myblog.repository.ArticleRepository;
import org.myblog.repository.UserRepository;
import org.myblog.service.AdminService;
import org.myblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping
    public String adminPage(Model model) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "admin";
    }
    @PostMapping("/articles")
    public String createArticle(
            @RequestParam String title,
            @RequestParam String body,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        adminService.createArticle(title, body, user);
        return "redirect:/admin";
    }
    @PostMapping("/articles/{id}")
    public String deleteArticle(@PathVariable int id, @RequestParam("_method") String method) {
        if ("delete".equalsIgnoreCase(method)) {
            adminService.deleteArticle(id);
        }
        return "redirect:/admin";
    }
    @GetMapping("/edit/{id}")
    public String editArticle(@PathVariable int id, Model model) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Статья не найдена"));
        model.addAttribute("article", article);
        return "edit";
    }
//    @PostMapping("/articles/{id}/edit")
//    public String updateArticle(@PathVariable int id,
//                                @RequestParam String title,
//                                @RequestParam String body) {
//        adminService.updateArticle(id, title, body);
//        return "redirect:/admin";
//    }
}
