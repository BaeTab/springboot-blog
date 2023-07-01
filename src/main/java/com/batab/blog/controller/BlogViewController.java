package com.batab.blog.controller;

import com.batab.blog.domain.Article;
import com.batab.blog.domain.Comment;
import com.batab.blog.dto.ArticleListViewResponse;
import com.batab.blog.dto.ArticleViewResponse;
import com.batab.blog.dto.CommentDTO;
import com.batab.blog.service.BlogService;
import com.batab.blog.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BlogViewController {

    private final BlogService blogService;
    private final CommentService commentService;




    public BlogViewController(BlogService blogService, CommentService commentService) {
        this.blogService = blogService;
        this.commentService = commentService;

    }

    private String getTruncatedAuthor(Authentication authentication) {
        String email = authentication.getName();
        String truncatedAuthor = email.substring(0, email.indexOf('@'));
        return truncatedAuthor;

    }

    @GetMapping("/articles")
    public String getArticles(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(required = false) String keyword,
                              Model model, Authentication authentication) {
        int pageSize = 10;

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").descending());
        Page<Article> articlePage;
        if (keyword != null && !keyword.isEmpty()) {
            // If a keyword is provided, search for articles based on the keyword
            articlePage = blogService.searchArticlesByKeyword(keyword, pageable);
        } else {
            // If no keyword is provided, fetch all articles
            articlePage = blogService.findAll(pageable);
        }

        List<ArticleListViewResponse> articles = articlePage.getContent()
                .stream()
                .map(article -> {
                    ArticleListViewResponse response = new ArticleListViewResponse(article);
                    response.setLikeCount(article.getLikeCount());
                    return response;
                })                .collect(Collectors.toList());


        model.addAttribute("articles", articles);
        model.addAttribute("truncatedAuthor", getTruncatedAuthor(authentication));
        model.addAttribute("email", authentication.getName());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());


        return "articleList";
    }


    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model, Authentication authentication) {
        Article article = blogService.findById(id);

        String contentWithLineBreaks = article.getContent().replace("\n", "<br/>");
        model.addAttribute("article", new ArticleViewResponse(article));
        model.addAttribute("contentWithLineBreaks", contentWithLineBreaks);
        model.addAttribute("article", article);
        model.addAttribute("truncatedAuthor", getTruncatedAuthor(authentication));
        model.addAttribute("email", authentication.getName());


        int page = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("updatedAt").descending());
        Page<Article> articlePage = blogService.findAll(pageable);

        List<ArticleListViewResponse> articles = articlePage.getContent()
                .stream()
                .map(ArticleListViewResponse::new)
                .collect(Collectors.toList());

        model.addAttribute("articles", articles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());

        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model, Authentication authentication) {
        ArticleViewResponse articleViewResponse = new ArticleViewResponse();

        if (id != null) {
            Article article = blogService.findById(id);
            articleViewResponse = new ArticleViewResponse(article);
        }

        String author = getTruncatedAuthor(authentication);
        articleViewResponse.setAuthor(author);

        model.addAttribute("article", articleViewResponse);
        model.addAttribute("truncatedAuthor", getTruncatedAuthor(authentication));
        model.addAttribute("email", authentication.getName());


        return "newArticle";
    }



    @PostMapping("/articles/{id}/comments")
    public String addComment(@PathVariable Long id, @ModelAttribute CommentDTO commentDTO, Model model, Authentication authentication) {
        String author = authentication.getName();
        commentDTO.setAuthor(author);
        commentService.addComment(id, commentDTO, author);

        Article article = blogService.findById(id);
        ArticleViewResponse articleViewResponse = new ArticleViewResponse(article);
        String contentWithLineBreaks = article.getContent().replace("\n", "<br/>");
        model.addAttribute("contentWithLineBreaks", contentWithLineBreaks);
        model.addAttribute("article", articleViewResponse);
        model.addAttribute("truncatedAuthor", getTruncatedAuthor(authentication));
        model.addAttribute("email", authentication.getName());


        int page = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("updatedAt").descending());
        Page<Article> articlePage = blogService.findAll(pageable);

        List<ArticleListViewResponse> articles = articlePage.getContent()
                .stream()
                .map(ArticleListViewResponse::new)
                .collect(Collectors.toList());

        model.addAttribute("articles", articles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());

        return "article";
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, Model model, Authentication authentication) {
        String currentUserEmail = authentication.getName();
        commentService.deleteComment(commentId, currentUserEmail);

        // Retrieve the updated article and its comments
        Article article = blogService.findById(articleId);
        List<Comment> comments = article.getComments();

        // Pass the article and comments to the view
        model.addAttribute("article", new ArticleViewResponse(article));
        model.addAttribute("comments", comments);
        model.addAttribute("truncatedAuthor", getTruncatedAuthor(authentication));
        model.addAttribute("email", authentication.getName());

        return "article";
    }

}
