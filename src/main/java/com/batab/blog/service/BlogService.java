package com.batab.blog.service;

import com.batab.blog.domain.Article;
import com.batab.blog.domain.LikedUser;
import com.batab.blog.dto.AddArticleRequest;
import com.batab.blog.dto.UpdateArticleRequest;
import com.batab.blog.dto.UpdatedArticleResponse;
import com.batab.blog.repository.BlogRepository;
import com.batab.blog.repository.LikedUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final Set<Long> likedArticlesByUser = new HashSet<>();
    private final LikedUserRepository likedUserRepository;


    public Article save(AddArticleRequest request, String author) {
        Article article = request.toEntity();
        article.setAuthor(author);
        Article savedArticle = blogRepository.save(article);
        return savedArticle;
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Page<Article> findAll(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public Page<Article> searchArticlesByKeyword(String keyword, Pageable pageable) {
        return blogRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
    }

    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public UpdatedArticleResponse update(long id, UpdateArticleRequest request, String currentUserEmail) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        if (!article.getAuthor().equals(currentUserEmail)) {
            throw new AccessDeniedException("Unauthorized to edit this article.");
        }
        article.update(request.getTitle(), request.getContent());
        Article updatedArticle = blogRepository.save(article);

        UpdatedArticleResponse response = new UpdatedArticleResponse();
        response.setId(updatedArticle.getId());
        response.setTitle(updatedArticle.getTitle());
        response.setContent(updatedArticle.getContent());

        return response;
    }

    @Transactional
    public void delete(long id, String currentUserEmail) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        if (!article.getAuthor().equals(currentUserEmail)) {
            throw new AccessDeniedException("Unauthorized to delete this article.");
        }
        likedUserRepository.deleteByPostId(id);
        blogRepository.delete(article);
    }


    @Transactional
    public void likeArticle(long id, String email) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        if (checkUserLikedArticle(id, email)) {
            return;
        }

        article.setLikeCount(article.getLikeCount() + 1);
        likedUserRepository.save(new LikedUser(email, id));

    }

    public boolean checkUserLikedArticle(long id, String email) {
        return likedUserRepository.existsByEmailAndPostId(email, id);
    }
}
