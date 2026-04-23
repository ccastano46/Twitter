package eci.arem.twitter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Swagger
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Public endpoints
                        .requestMatchers(HttpMethod.GET, "/posts/stream").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/users/**").permitAll()
                        // Protected endpoints
                        .requestMatchers(HttpMethod.GET, "/user/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/user/register").authenticated()
                        .requestMatchers(HttpMethod.POST, "/posts/create").authenticated()
                        .requestMatchers(HttpMethod.GET, "/posts/user").authenticated()
                        // The rest has to be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}