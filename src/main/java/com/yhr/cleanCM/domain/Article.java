package com.yhr.cleanCM.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private LocalDateTime regDate = LocalDateTime.now();
    private LocalDateTime updateDate = LocalDateTime.now();

    // 생성 메소드
    public static Article createArticle( String title, String body ) {

        Article article = new Article();

        article.title = title;
        article.body = body;

        return article;

    }

    public  void modifyArticle(String title, String body){
        this.title = title;
        this.body = body;

        this.updateDate = LocalDateTime.now();
    }

    //  연관관계 메소드
    public void setMember(Member member) {

        this.member = member;
        member.getArticles().add(this);

    }

    public void setBoard(Board board) {
        this.board = board;
        board.getArticles().add(this);

    }
}
