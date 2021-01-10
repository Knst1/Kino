package ru.rsoi.frontend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/movies**", "/login**", "/error")
                .permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .csrf().disable()
                .oauth2Login();
    }
}
