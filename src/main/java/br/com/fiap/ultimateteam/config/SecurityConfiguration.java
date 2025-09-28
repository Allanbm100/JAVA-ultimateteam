package br.com.fiap.ultimateteam.config;

import br.com.fiap.ultimateteam.auth.AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfiguration {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final TeamRequiredFilter teamRequiredFilter;

    public SecurityConfiguration(AuthenticationSuccessHandler authenticationSuccessHandler, TeamRequiredFilter teamRequiredFilter) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.teamRequiredFilter = teamRequiredFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/logout", "/error", "/webjars/**", "/*.css", "/*.js", "/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login( login -> login
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler)
                        .permitAll()
                )
                .logout( logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .addFilterBefore(teamRequiredFilter, AuthorizationFilter.class)
                .build();
    }

}
