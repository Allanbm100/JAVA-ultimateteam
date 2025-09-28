package br.com.fiap.ultimateteam.config;

import br.com.fiap.ultimateteam.user.User;
import br.com.fiap.ultimateteam.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamRequiredFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    private final List<String> allowedPaths = List.of("/team/new", "/team", "/logout", "/error");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestURI = request.getRequestURI();

        if (authentication == null || !authentication.isAuthenticated() || isAllowedPath(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authentication.getPrincipal() instanceof OAuth2User principal) {
            userRepository.findByEmail(principal.getAttribute("email")).ifPresent(user -> {
                if (user.getTeam() == null) {
                    try {
                        response.sendRedirect("/team/new");
                    } catch (IOException e) {
                        logger.error("Erro ao redirecionar para a criação de time", e);
                    }
                } else {
                    try {
                        filterChain.doFilter(request, response);
                    } catch (IOException | ServletException e) {
                        logger.error("Erro no filtro após verificação de time", e);
                    }
                }
            });
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isAllowedPath(String path) {
        return allowedPaths.stream().anyMatch(path::equals) || path.startsWith("/uploads") || path.startsWith("/webjars") || path.endsWith(".css") || path.endsWith(".js");
    }
}
