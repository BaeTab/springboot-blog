package com.batab.blog.dto;

import com.batab.blog.domain.Article;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String updatedAt;
    private final boolean hasComments;
    private final String author;
    private int commentCount;
    private int likeCount;






    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = truncateText(article.getTitle(), 20);  //제목 20글자 까지만 표시
        this.content = truncateText(article.getContent(), 20); //내용 10글자 까지만 표시
        this.author = article.getAuthor();
        this.updatedAt = article.getUpdatedAt() != null ? article.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : null;
        this.hasComments = !article.getComments().isEmpty();
        this.commentCount = article.getComments().size();
    }



    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text; // maxLength 넘지 않으면 텍스트 모두 표시
        } else {
            String truncatedText = text.substring(0, maxLength); //maxLength 넘으면 자르고 ... 넣기
            return truncatedText + "...";
        }
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
