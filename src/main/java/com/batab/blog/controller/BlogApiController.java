package com.batab.blog.controller;

import com.batab.blog.domain.Article;
import com.batab.blog.domain.LikedUser;
import com.batab.blog.dto.*;
import com.batab.blog.repository.LikedUserRepository;
import com.batab.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;
    private final LikedUserRepository likedUserRepository;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Authentication authentication) {
        String author = authentication.getName();
        Article savedArticle = blogService.save(request,author);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<UpdatedArticleResponse> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request, Authentication authentication) {
        String currentUserEmail = authentication.getName();
        UpdatedArticleResponse updatedArticle = blogService.update(id, request, currentUserEmail);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }


    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id, Authentication authentication) {
        String currentUserEmail = authentication.getName();
        blogService.delete(id, currentUserEmail);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}/like")
    public ResponseEntity<Void> likeArticle(@PathVariable Long id, @RequestBody LikeRequest request, Authentication authentication) {
        String userEmail = authentication.getName();
        boolean hasLiked = blogService.checkUserLikedArticle(id, userEmail);

        if (hasLiked) {
            return ResponseEntity.badRequest().build();
        }

        blogService.likeArticle(id, userEmail);
        likedUserRepository.save(new LikedUser(userEmail, id));

        return ResponseEntity.ok().build();
    }


}


