package com.quanxiaoha.weblog.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // 配置安全设置

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/admin/**") // 认证所有以 /admin 为前缀的 URL 资源
                .authenticated()
                .anyRequest()
                .permitAll()  // 其他都需要放行，无需认证
                .and()
                .formLogin() // 使用表单登录
                .and()
                .httpBasic(); // 使用 HTTP Basic 认证

    }
}

