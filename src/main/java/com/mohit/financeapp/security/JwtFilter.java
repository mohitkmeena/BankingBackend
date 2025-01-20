package com.mohit.financeapp.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Configuration
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    // to prevent cyclic dependency 
   //   private UserDetailsService userDetailsService;
    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
                  String token=getTokenFromRequest(request);
                  String username=null;
                  if(token!=null){
                    username=jwtTokenProvider.getUsername(token);
                    UserDetails userDetails=context.getBean(UserDetailsService.class).loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken( userDetails,null,userDetails.getAuthorities());
                     authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                     SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                  }
                  filterChain.doFilter(request, response);

       
      }
    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken=request.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer")){
             //token start from 7
             System.out.println(bearerToken.substring(7));
            return bearerToken.substring(7);
        }
        return null;
    }
}
