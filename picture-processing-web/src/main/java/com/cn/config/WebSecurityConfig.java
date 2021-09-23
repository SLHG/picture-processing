package com.cn.config;

import com.alibaba.fastjson.JSON;
import com.cn.beans.common.ResultBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final
    UserDetailsService userDetailsService;
    final
    LoginSuccessHandler loginSuccessHandler;
    final
    PasswordEncoder passwordEncoder;


    public WebSecurityConfig(UserDetailsService userDetailsService, LoginSuccessHandler loginSuccessHandler, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    //http请求拦截配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();//开启运行iframe嵌套页面
        //1、配置权限认证
        http.authorizeRequests()
                .antMatchers("/wx/*").permitAll()
                .antMatchers("/manager/user/add").hasRole("ADMIN")
                .antMatchers("/manager/user/delete").hasRole("ADMIN")
                .antMatchers("/manager/user/get").hasAnyRole("ADMIN", "USER")
                .antMatchers("/manager/user/save").hasAnyRole("ADMIN", "USER")
                .antMatchers("/manager/login").permitAll()
                .anyRequest() //任何其它请求
                .authenticated() //都需要身份认证
                .and()
                //2、登录配置表单认证方式
                .formLogin()
                .loginPage("/public/login.html")//自定义登录页面的url
                .usernameParameter("username")//设置登录账号参数，与表单参数一致
                .passwordParameter("password")//设置登录密码参数，与表单参数一致
                // 告诉Spring Security在发送指定路径时处理提交的凭证，默认情况下，将用户重定向回用户来自的页面。登录表单form中action的地址，也就是处理认证请求的路径，
                // 只要保持表单中action和HttpSecurity里配置的loginProcessingUrl一致就可以了，也不用自己去处理，它不会将请求传递给Spring MVC和您的控制器，所以我们就不需要自己再去写一个/user/login的控制器接口了
                .loginProcessingUrl("/manager/login")//配置默认登录入口
                .successHandler(loginSuccessHandler)
                .failureHandler((request, response, e) -> {
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(new ResultBean(ResultBean.FAIL_CODE, "登录失败")));
                    writer.flush();
                    writer.close();
                })
                .and()
                //3、注销
                .logout()
                .logoutUrl("/manager/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(new ResultBean(ResultBean.SUCCESS_CODE, "登出成功")));
                    writer.flush();
                    writer.close();
                })
                .permitAll()
                .and()
                //4、session管理
                .sessionManagement()
                .invalidSessionUrl("/public/login.html") //失效后跳转到登陆页面
                //单用户登录，如果有一个登录了，同一个用户在其他地方登录将前一个剔除下线
                //.maximumSessions(1).expiredSessionStrategy(expiredSessionStrategy())
                //单用户登录，如果有一个登录了，同一个用户在其他地方不能登录
                //.maximumSessions(1).maxSessionsPreventsLogin(true) ;
                .and()
                //5、禁用跨站csrf攻击防御
                .csrf()
                .disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/public/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
