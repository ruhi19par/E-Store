package com.example.lcwd.Electronic.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.lcwd.Electronic.Security.JwtHelper;
import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private Logger logger=LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException,IOException{

        String path=request.getRequestURI(); // 🔥 FIXED

        // 🔥 STRONGER SKIP
        if(path.contains("swagger") ||
                path.contains("api-docs") ||
                path.contains("webjars") ||
                path.contains("/auth/login") ||
                (path.contains("/users") && request.getMethod().equals("POST"))){

            filterChain.doFilter(request,response);
            return;
        }

        System.out.println("JWT FILTER HIT");

        String requestHeader=request.getHeader("Authorization");

        String username=null;
        String token=null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer ")){
            token=requestHeader.substring(7);

            try{
                username=jwtHelper.getUsernameFromToken(token);
            }catch(ExpiredJwtException e){
                logger.error("Token expired!");
            }catch(Exception e){
                logger.error("JWT ERROR",e);
            }

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                UserDetails userDetails=userDetailsService.loadUserByUsername(username);

                Boolean valid=jwtHelper.validateToken(token,userDetails);

                if(valid){
                    UsernamePasswordAuthenticationToken auth=
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request,response);
    }
}
