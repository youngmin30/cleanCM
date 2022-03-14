package com.yhr.cleanCM.controller;

import com.yhr.cleanCM.service.AdmService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adm")
@RequiredArgsConstructor
public class AdmController {

    private final AdmService admService;

    @GetMapping("/page")
    public String showAdminPage(Model model){

        model.addAttribute("memberStatData", admService.getMemberStatDto());
        model.addAttribute("boardStatData", admService.getBoardStatDto());
        model.addAttribute("articleStatData", admService.getArticleStatDto());

        return "adm/general/main";
    }


}
