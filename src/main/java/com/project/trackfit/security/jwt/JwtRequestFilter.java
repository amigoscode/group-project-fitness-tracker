package com.project.trackfit.security.jwt;


import com.project.trackfit.core.model.UserProfile;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ApplicationConfig userDetailsService;

    private final JwtService jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;
        String userProfile= null;

        if (
                authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
        ) {
            token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);
            userProfile= jwtUtil.extractUserType(token);


        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //TODO: UPDATE THE CLAIMS TO HAVE PROFILE TYPE
            UserDetails userDetails;

            if(Objects.equals(userProfile, UserProfile.CUSTOMER.toString())) {
              userDetails = this.userDetailsService.customerDetailsService(username);
            }
            else {
                userDetails = this.userDetailsService.trainerDetailsService(username);
            }

            if (jwtUtil.validateToken(token, userDetails)) {
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}