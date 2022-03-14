package com.yhr.cleanCM.dto.article;

import com.yhr.cleanCM.domain.Article;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleDTO {

    private Long id;

    private String title;
    private String body;

    private String memberLoginId;
    private String authorName;

    private Long boardId;
    private String boardName;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    public ArticleDTO(Article article) {

        this.id = article.getId();

        this.title = article.getTitle();

        this.body = article.getBody();
        this.memberLoginId = article.getMember().getLoginId();
        this.authorName = article.getMember().getNickname();

        this.boardId = article.getBoard().getId();
        this.boardName = article.getBoard().getName();

        this.regDate = article.getRegDate();
        this.updateDate = article.getUpdateDate();

    }

}
