package com.yhr.cleanCM.controller;

import com.yhr.cleanCM.service.AdmArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm")
public class AdmArticleController {

    private final AdmArticleService admArticleService;

    // 게시글 리스트
    @GetMapping("/articles")
    public String showManageArticle(Model model){

        model.addAttribute("articles", admArticleService.getArticleList());

        return "adm/article/main";
    }

    // 게시글 삭제
    @GetMapping("/articles/delete/{id}")
    public String doDeleteArticle(@PathVariable(name = "id") Long id){

        admArticleService.deleteArticle(id);

        return "redirect:/adm/articles";
    }


}
