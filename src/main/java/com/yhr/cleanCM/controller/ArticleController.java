package com.yhr.cleanCM.controller;

import com.yhr.cleanCM.domain.Article;
import com.yhr.cleanCM.domain.Board;
import com.yhr.cleanCM.domain.Member;
import com.yhr.cleanCM.dto.article.ArticleDTO;
import com.yhr.cleanCM.dto.article.ArticleModifyForm;
import com.yhr.cleanCM.dto.article.ArticleSaveForm;
import com.yhr.cleanCM.dto.board.BoardDTO;
import com.yhr.cleanCM.service.ArticleService;
import com.yhr.cleanCM.service.BoardService;
import com.yhr.cleanCM.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/boards/{id}/articles/write")
    public String showArticleWrite(@PathVariable(name = "id")Long id, Model model) {

        ArticleDTO findArticle = articleService.getArticle(id);

        model.addAttribute("boardName", findArticle.getBoardName());
        model.addAttribute("boardId", findArticle.getBoardId());
        model.addAttribute("articleSaveForm", new ArticleSaveForm());

        return "usr/article/write";

    }

    @PostMapping("/boards/{id}/articles/write")
    public String doWrite(@Validated ArticleSaveForm articleSaveForm, BindingResult bindingResult, Model model, Principal principal, @PathVariable(name = "id")Long id) {

        if (bindingResult.hasErrors()) {

            ArticleDTO findArticle = articleService.getArticle(id);

            model.addAttribute("boardName", findArticle.getBoardName());
            model.addAttribute("boardId", findArticle.getBoardId());

            return "usr/article/write";
        }

        try {

            Board findBoard = boardService.getBoard(articleSaveForm.getBoard_id());
            Member findMember = memberService.findByLoginId(principal.getName());

            articleService.save(
                    articleSaveForm,
                    findMember,
                    findBoard
            );

        } catch (IllegalStateException e) {

            model.addAttribute("err_msg", e.getMessage());

            return "usr/article/write";

        }

        return "redirect:/articles";

    }

    @GetMapping("/articles/modify/{id}")
    public String showModify(@PathVariable(name = "id") Long id, Model model){

        try {
            ArticleDTO findArticle = articleService.getArticle(id);

            model.addAttribute("articleId", findArticle.getId());
            model.addAttribute("boardName", findArticle.getBoardName());
            model.addAttribute("boardId", findArticle.getBoardId());
            model.addAttribute("articleModifyForm", new ArticleModifyForm(findArticle));

            return "usr/article/modify";
        }catch (Exception e){
            return "redirect:/";
        }
    }

    @PostMapping("/articles/modify/{id}")
    public String doModify(@PathVariable(name = "id") Long id, @Validated ArticleModifyForm articleModifyForm, BindingResult bindingResult, Principal principal, Model model){

        ArticleDTO findArticle = articleService.getArticle(id);

        if(bindingResult.hasErrors()){

            model.addAttribute("articleId", findArticle.getId());
            model.addAttribute("boardName", findArticle.getBoardName());
            model.addAttribute("boardId", findArticle.getBoardId());

            return "usr/article/modify";
        }

        try{

            if(!findArticle.getMemberLoginId().equals(principal.getName())){
                throw new IllegalStateException("잘못된 요청입니다.");
            }

            Board findBoard = boardService.getBoard(articleModifyForm.getBoard_id());

            articleService.modifyArticle(articleModifyForm, findBoard, id);
            return "redirect:/boards/"+ id;
        }catch (Exception e){
            return "redirect:/articles/modify/" + id;
        }
    }

    @GetMapping("/articles")
    public String showList(Model model) {

        List<ArticleDTO> articleList = articleService.getList();

        ArticleDTO articleDTO = articleList.get(0);

        model.addAttribute("boardName", articleDTO.getBoardName());
        model.addAttribute("articleList", articleList);

        return "usr/article/list";

    }

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable(name = "id") Long id, Principal principal){

        try {
            ArticleDTO article = articleService.getArticle(id);

            if(!article.getMemberLoginId().equals(principal.getName())){
                return "redirect:/boards/" + id;
            }
            articleService.delete(id);
            return "redirect:/boards";

        }catch (Exception e){
            return "redirect:/";
        }

    }

    @GetMapping("/articles/{id}")
    public String showDetail(@PathVariable(name = "id") Long id, Model model){

        try {
            ArticleDTO findArticle = articleService.getArticle(id);
            model.addAttribute("article", findArticle);

            return "usr/article/detail";
        }catch (Exception e){
            return "redirect:/";
        }

    }


}