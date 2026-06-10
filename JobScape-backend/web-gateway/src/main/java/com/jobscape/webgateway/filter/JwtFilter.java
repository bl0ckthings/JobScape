package com.jobscape.webgateway.filter;

import com.jobscape.webgateway.configuration.JwtUtils;
import com.jobscape.webgateway.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Cette méthode va nous permettre de filter les requêtes et vérifier le JWT.
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        // Ici on check si le header existe et s'il commence par Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Le token JWT étant toujours suivi de Bearer,
            // on le récupère en faisant un substring à partir du 7 eme caractere (espace inclus)
            jwt = authHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
        }
        // On s'assure que l'user n'est pas authentifié ( premiere connexion)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (jwtUtils.validateToken(jwt, userDetails)) {
                // On crée un nouveau token d'authentification
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Récupérer les informations de la requête, pour des raisons de logs
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
