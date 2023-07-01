package com.batab.blog.dto;

import com.batab.blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddArticleRequest {

    private String title;
    private String content;
    private String author;



    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}

