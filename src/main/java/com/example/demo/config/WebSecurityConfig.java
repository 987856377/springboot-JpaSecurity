package com.example.demo.config;

import com.example.demo.jwt.JWTAuthenticationFilter;
import com.example.demo.jwt.JWTLoginFilter;
import com.example.demo.jwt.JwtAuthenticationTokenFilter;
import com.example.demo.security.CustomAuthenticationProvider;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.service.UserService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)// 控制权限注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    @Resource
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/dba/**").hasRole("DBA")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/signin","/signup","/").permitAll()
                .anyRequest().authenticated()//所有请求必须登陆后访问
                .and()
                .formLogin()
                .loginPage("/signin")
                .failureUrl("/login?error")
                .permitAll()//登录界面，错误界面可以直接访问
                .and()
                .logout()
                .permitAll();//注销请求可直接访问
//        http
//                .addFilter(new JWTLoginFilter(authenticationManager()))
//                .addFilter(new JWTAuthenticationFilter(authenticationManager()));

        // 基于token，所以不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 添加JWT filter
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
        http.headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//        设置一个内存中的用户密码，早期的版本不需要设置密码登陆，springsecurity5之后需要
//        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("user1")
//                .password(passwordEncoder().encode("123456"))
//                .roles("USER");

//        在用户注册的时候将用户密码加密, 验证的时候使用默认配置进行登录验证, 数据库中用户密码为密文
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

//        在用户注册的时候不将用户密码加密, 验证的时候使用自定义类 CustomAuthenticationProvider 验证, 数据库中用户密码为明文
        auth.authenticationProvider(customAuthenticationProvider);
    }

}
