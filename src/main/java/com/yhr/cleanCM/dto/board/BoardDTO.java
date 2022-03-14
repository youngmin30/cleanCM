package com.yhr.cleanCM.dto.board;

import com.yhr.cleanCM.domain.Board;
import com.yhr.cleanCM.dto.article.ArticleListDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardDTO {

    private Long id;

    private String name;
    private String detail;

    private List<ArticleListDTO> articleListDTO;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;


    public BoardDTO(Board board, List<ArticleListDTO> articleListDTO){

        this.id = board.getId();
        this.name = board.getName();
        this.detail = board.getDetail();
        this.articleListDTO = articleListDTO;
        this.regDate = board.getRegDate();
        this.updateDate = board.getUpdateDate();

    }
}
