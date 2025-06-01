package org.myblog.controller;

import org.myblog.model.Article;
import org.myblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/preview")
    public String preview(Model model) {
        var articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "preview";
    }
    @GetMapping("/main")
    public String mainPage(Model model, Principal principal) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "main";
    }
}
