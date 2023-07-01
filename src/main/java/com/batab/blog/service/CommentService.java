package com.batab.blog.service;

import com.batab.blog.domain.Article;
import com.batab.blog.domain.Comment;
import com.batab.blog.dto.CommentDTO;
import com.batab.blog.repository.BlogRepository;
import com.batab.blog.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository articleRepository;

    public CommentService(CommentRepository commentRepository, BlogRepository blogRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = blogRepository;
    }

    @Transactional
    public void addComment(Long articleId, CommentDTO commentDTO, String currentUserEmail) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을수 없습니다"));

        Comment comment = new Comment();
        comment.setAuthor(currentUserEmail);
        comment.setContent(commentDTO.getContent());
        comment.setArticle(article);

        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String currentUserEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().equals(currentUserEmail)) {
            throw new RuntimeException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

}
