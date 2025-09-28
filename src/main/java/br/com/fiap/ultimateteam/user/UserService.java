package br.com.fiap.ultimateteam.user;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(OAuth2User principal) {
        String email = principal.getAttribute("email");
        if (email == null) {
            throw new IllegalStateException("Email não encontrado no provedor OAuth2. Verifique se seu e-mail é público no GitHub.");
        }

        User user = userRepository.findByEmail(email).orElse(new User());

        user.setEmail(email);

        String name = principal.getAttribute("name");
        if (name == null) {
            name = principal.getAttribute("login");
        }
        user.setName(name);

        String avatarUrl = principal.getAttribute("picture");
        if (avatarUrl == null) {
            avatarUrl = principal.getAttribute("avatar_url");
        }
        user.setAvatarUrl(avatarUrl);

        return userRepository.save(user);
    }
}
