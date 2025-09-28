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

    // Lista de caminhos que não exigem um time
    private final List<String> allowedPaths = List.of("/team/new", "/team", "/logout", "/error");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestURI = request.getRequestURI();

        // Se o usuário não está autenticado, ou está acessando um caminho permitido, ou um recurso estático, deixa passar.
        if (authentication == null || !authentication.isAuthenticated() || isAllowedPath(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Se chegou aqui, o usuário está autenticado. Vamos verificar se ele tem um time.
        if (authentication.getPrincipal() instanceof OAuth2User principal) {
            userRepository.findByEmail(principal.getAttribute("email")).ifPresent(user -> {
                if (user.getTeam() == null) {
                    try {
                        // Se não tem time, redireciona para a criação.
                        response.sendRedirect("/team/new");
                    } catch (IOException e) {
                        logger.error("Erro ao redirecionar para a criação de time", e);
                    }
                } else {
                    try {
                        // Se tem time, deixa a requisição continuar.
                        filterChain.doFilter(request, response);
                    } catch (IOException | ServletException e) {
                        logger.error("Erro no filtro após verificação de time", e);
                    }
                }
            });
        } else {
            // Caso o principal não seja OAuth2User, apenas continua.
            filterChain.doFilter(request, response);
        }
    }

    private boolean isAllowedPath(String path) {
        // Verifica se o caminho está na lista de permitidos ou se é um recurso estático.
        return allowedPaths.stream().anyMatch(path::equals) || path.startsWith("/uploads") || path.startsWith("/webjars") || path.endsWith(".css") || path.endsWith(".js");
    }
}
