package com.batab.blog.dto;

import com.batab.blog.domain.Article;
import com.batab.blog.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArticleViewResponse {

  private Long id;
  private String title;
  private String content;
  private String author;
  private LocalDateTime createdAt;
  private List<Comment> comments;
  private int likeCount;


  public ArticleViewResponse(Article article) {
    this.id = article.getId();
    this.title = article.getTitle();
    this.content = article.getContent();
    this.createdAt = article.getCreatedAt();
    this.comments = article.getComments();
    this.author = article.getAuthor();
  }


  public void setAuthor(String author) {
    this.author = author;
  }


}

