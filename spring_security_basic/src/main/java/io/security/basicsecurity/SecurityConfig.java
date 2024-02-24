package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Autowired;import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests() //요청에 대한 보안 검사
                .anyRequest().authenticated(); //어떤 보안 요청에도 인증을 받도록
        http.formLogin(); //인증은 기본적으로 formLogin 방식으로

        http.logout()   //로그아웃 처리
                .logoutUrl("/logout") //로그아웃 처리 URL
                .logoutSuccessUrl("/login") //로그아웃 성공 후 이동페이지
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        HttpSession session  = request.getSession();
                        session.invalidate();
                    }
                }) //로그아웃 핸들러
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.sendRedirect("/login");
                    }
                }) //로그아웃 성공 후 핸들러
                .deleteCookies("remember-me"); //삭제하고 싶은 쿠키명

        http.rememberMe()
                .rememberMeParameter("remember") //기본 파라미터명은 remember-me
                .tokenValiditySeconds(3600) //Default는 14일
                .alwaysRemember(false) //remember me 기능이 활성화되지 않아도 항상 실행
                .userDetailsService(userDetailsService)
                ;
    }
}
