package com.quanxiaoha.weblog.admin.config;

import com.quanxiaoha.weblog.jwt.config.JwtAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // 配置安全设置

    @Autowired
    private JwtAuthenticationSecurityConfig jwtAuthenticationSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(). // 禁用 csrf
                formLogin().disable() // 禁用表单登录
                .apply(jwtAuthenticationSecurityConfig) // 设置用户登录认证相关配置
                .and()
                .authorizeHttpRequests()
                .mvcMatchers("/admin/**").authenticated() // 认证所有以 /admin 为前缀的 URL 资源
                .anyRequest().permitAll() // 其他都需要放行，无需认证
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 前后端分离，无需创建会话
    }

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.authorizeHttpRequests()
    //             .mvcMatchers("/admin/**") // 认证所有以 /admin 为前缀的 URL 资源
    //             .authenticated()
    //             .anyRequest()
    //             .permitAll()  // 其他都需要放行，无需认证
    //             .and()
    //             .formLogin() // 使用表单登录
    //             .and()
    //             .httpBasic(); // 使用 HTTP Basic 认证
    //
    // }
}

