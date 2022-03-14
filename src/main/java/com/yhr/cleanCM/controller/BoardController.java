package com.yhr.cleanCM.controller;

import com.yhr.cleanCM.domain.Board;
import com.yhr.cleanCM.domain.Member;
import com.yhr.cleanCM.dto.article.ArticleListDTO;
import com.yhr.cleanCM.dto.board.BoardDTO;
import com.yhr.cleanCM.dto.board.BoardModifyForm;
import com.yhr.cleanCM.dto.board.BoardSaveForm;
import com.yhr.cleanCM.service.BoardService;
import com.yhr.cleanCM.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public String showBoardList(Model model) {

        List<Board> boardList = boardService.findAll();

        model.addAttribute("boardList", boardList);

        return "adm/board/list";

    }

    @GetMapping("/boards/{id}") // http://localhost:8085/boards/id?page=1&searchKeyword=제목
    public String showBoardDetail(@PathVariable(name = "id") Long id, Model model, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword) {

        int size = 10;

        try {

            BoardDTO boardDetail = boardService.getBoardDetail(id);

            List<ArticleListDTO> articleListDTO = boardDetail.getArticleListDTO();

            List<ArticleListDTO> store = new ArrayList<>();

            for( ArticleListDTO listDTO : articleListDTO ){

                if( listDTO.getTitle().contains(searchKeyword) ){
                    store.add(listDTO);
                }
            }

            if( store.size() != 0 ){
                articleListDTO = store;
            }

            Collections.reverse(articleListDTO);

            int startIndex = (page - 1) * size;

            int lastIndex = ((page -1) * size) + 9;

            int lastPage = (int)Math.ceil(articleListDTO.size()/(double)size);

            if( page == lastPage ){

                lastIndex = articleListDTO.size();

            }else if( page > lastPage ){

                return "redirect:/";

            }else{
                lastIndex += 1;
            }

            List<ArticleListDTO> articlePage = articleListDTO.subList(startIndex, lastIndex);

            if( !searchKeyword.equals("") && store.size() == 0 ){
                articlePage = store;
            }

            model.addAttribute("board", boardDetail);
            model.addAttribute("articles", articlePage);
            model.addAttribute("maxPage", lastPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", searchKeyword);

        } catch (Exception e) {
            return "redirect:/";
        }

        return "adm/board/detail";

    }




}
