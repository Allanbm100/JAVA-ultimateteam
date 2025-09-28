package br.com.fiap.ultimateteam.auth;

import br.com.fiap.ultimateteam.user.User;
import br.com.fiap.ultimateteam.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    public AuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        User user = userService.register(principal);

        if (user.getTeam() == null) {
            log.info("Usuário sem time. Redirecionando para /team/new");
            getRedirectStrategy().sendRedirect(request, response, "/team/new");
        } else {
            log.info("Usuário com time. Redirecionando para a página padrão.");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
