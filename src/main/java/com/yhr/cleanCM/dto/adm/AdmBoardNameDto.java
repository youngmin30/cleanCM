package com.yhr.cleanCM.dto.adm;

import com.yhr.cleanCM.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmBoardNameDto {

    private String name;
    private LocalDateTime regDate;

    public AdmBoardNameDto(Board board){

        this.name = board.getName();
        this.regDate = board.getRegDate();

    }

}
