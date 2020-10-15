package com.mag.UrlShortener.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

  @Value("#{environment.SECRET_KEY}")
  String secretKey;

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {
    String header = httpServletRequest.getHeader("Authorization");
    if (header != null
        && header.startsWith("Bearer ")
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      String apiKey = header.substring(7);
      if (apiKey.equals(secretKey)) {
        var apiToken = new ApiKeyAuthenticationToken(apiKey, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(apiToken);
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
