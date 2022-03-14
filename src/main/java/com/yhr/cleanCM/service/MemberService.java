package com.yhr.cleanCM.service;

import com.yhr.cleanCM.config.Role;
import com.yhr.cleanCM.dao.MemberRepository;
import com.yhr.cleanCM.domain.Article;
import com.yhr.cleanCM.domain.Member;
import com.yhr.cleanCM.dto.article.ArticleDTO;
import com.yhr.cleanCM.dto.member.MemberModifyForm;
import com.yhr.cleanCM.dto.member.MemberSaveForm;
import com.yhr.cleanCM.dto.member.MyPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ArticleService articleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByLoginId(username).get();
    }

    /**
     * 회원 중복 체크
     * @param loginId
     * @param nickname
     * @param email
     */
    public void isDuplicateMember(String loginId, String nickname, String email){

        if(memberRepository.existsByLoginId(loginId)){
            throw new IllegalStateException("이미 존재하는 아이디 입니다");
        }else if(memberRepository.existsByNickname(nickname)){
            throw new IllegalStateException("이미 존재하는 아이디 입니다");
        }else if(memberRepository.existsByEmail(email)){
            throw new IllegalStateException("이미 존재하는 이메일 입니다.");
        }
    }

    /**
     * 회원가입
     * @param memberSaveForm
     */

    @Transactional
    public void save(MemberSaveForm memberSaveForm) throws IllegalStateException{

        isDuplicateMember(
                memberSaveForm.getLoginId(),
                memberSaveForm.getNickname(),
                memberSaveForm.getEmail()
        );

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Member member = Member.createMember(
                memberSaveForm.getLoginId(),
                bCryptPasswordEncoder.encode(memberSaveForm.getLoginPw()),
                memberSaveForm.getName(),
                memberSaveForm.getNickname(),
                memberSaveForm.getEmail(),
                Role.MEMBER
        );

        memberRepository.save(member);
    }

    public Member findById(Long id){

        Optional<Member> memberOptional = memberRepository.findById(id);

        memberOptional.orElseThrow(
                () -> new IllegalStateException("존재하지 않는 회원입니다.")
        );

        return memberOptional.get();

    }

    public Member findByLoginId(String loginId) throws IllegalStateException {

        Optional<Member> memberOptional = memberRepository.findByLoginId(loginId);

        memberOptional.orElseThrow(
                () -> new IllegalStateException("존재하지 않는 회원입니다")
        );

        return memberOptional.get();
    }

    public MyPageDTO getMyArticles(String loginId){

        List<ArticleDTO> articleDTOList = new ArrayList<>();

        Member findMember = findByLoginId(loginId);

        List<Article> articles = findMember.getArticles();

        Collections.reverse(articles);

        for( Article article : articles ){
            ArticleDTO findArticle = articleService.getArticle(article.getId());
            articleDTOList.add(findArticle);
        }

        return new MyPageDTO(findMember, articleDTOList);


    }


    @Transactional
    public Long modifyMember(MemberModifyForm memberModifyForm, String loginId) {

        Member findMember = findByLoginId(loginId);

        BCryptPasswordEncoder bCryptPasswordEncoder =  new BCryptPasswordEncoder();

        findMember.modifyMember(
                bCryptPasswordEncoder.encode(memberModifyForm.getLoginPw()),
                memberModifyForm.getNickname(),
                memberModifyForm.getEmail()
        );

        return findMember.getId();

    }

    public boolean isDupleLoginId(String loginId){
        return memberRepository.existsByLoginId(loginId);
    }

    public boolean idDupleNickname(String nickname){
        return memberRepository.existsByNickname(nickname);
    }

    public boolean isDupleEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public void changeTempPw( String pw, Member member ){

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    member.changePw(bCryptPasswordEncoder.encode(pw));

    }

    @Transactional
    public void deleteMember(String loginId) {

        Member findMember = findByLoginId(loginId);

        memberRepository.delete(findMember);

    }
}
