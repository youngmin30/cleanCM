package com.yhr.cleanCM.service;

import com.yhr.cleanCM.dao.MemberRepository;
import com.yhr.cleanCM.domain.Article;
import com.yhr.cleanCM.domain.Member;
import com.yhr.cleanCM.dto.adm.AdmMemberDetailDTO;
import com.yhr.cleanCM.dto.article.ArticleListDTO;
import com.yhr.cleanCM.dto.member.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdmMemberService {

    private final MemberRepository memberRepository;

    public List<MemberDTO> getMemberList(){

        List<MemberDTO> memberDTOList = new ArrayList<>();

        List<Member> memberList = memberRepository.findAll();

        for( Member member : memberList ){
            MemberDTO memberDTO = new MemberDTO(member);
            memberDTOList.add(memberDTO);
        }

        return memberDTOList;
    }

    public AdmMemberDetailDTO getMemberDetail(Long id){

        List<ArticleListDTO> articleDTOList = new ArrayList<>();

        Optional<Member> memberOptional = memberRepository.findById(id);
        
        memberOptional.orElseThrow(
                () -> new IllegalStateException("존재하지 않는 회원입니다.")
        );

        Member findMember = memberOptional.get();

        List<Article> articles = findMember.getArticles();

        for( Article article : articles ){

            ArticleListDTO articleListDTO = new ArticleListDTO(article);
            articleDTOList.add(articleListDTO);
        }
        return new AdmMemberDetailDTO(findMember, articleDTOList);

    }


    @Transactional
    public void banMember(Long id){

        Optional<Member> memberOptional = memberRepository.findById(id);

        memberOptional.orElseThrow(
                () -> new IllegalStateException("존재하지 않는 회원입니다.")
        );

        memberRepository.delete(memberOptional.get());

    }













}
