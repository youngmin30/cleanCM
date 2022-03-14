package com.yhr.cleanCM.domain;

import com.yhr.cleanCM.config.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member implements UserDetails {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginPw;
    private String loginId;

    private String name;
    private String nickname;
    private String email;

    private LocalDateTime regDate = LocalDateTime.now();
    private LocalDateTime updateDate = LocalDateTime.now();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Article> articles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role authority;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    public static Member createMember(String loginId, String loginPw, String name, String nickname, String email, Role authority) {

        Member member = new Member();

        member.loginId = loginId;
        member.loginPw = loginPw;

        member.name = name;
        member.nickname = nickname;
        member.email = email;

        member.authority = authority;

        return member;
    }

    public void modifyMember(String loginPw, String nickname, String email){

        this.loginPw = loginPw;
        this.nickname = nickname;
        this.email = email;

    }

    public void changePw( String pw ){

        this.loginPw = pw;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.authority.getValue()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return loginPw;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
