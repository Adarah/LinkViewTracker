package com.mag.UrlShortener.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class Config extends WebSecurityConfigurerAdapter {
  @Autowired ApiKeyFilter apiKeyFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf()
        .disable()
        .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/info/*")
        .authenticated()
        .antMatchers("/*")
        .permitAll();
  }
}
