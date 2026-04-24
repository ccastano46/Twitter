package eci.arem.twitter.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${AUTH0_AUDIENCE}")
    private String audience;

    @Value("${AUTH0_DOMAIN}")
    private String domain;

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
                        .requestMatchers(HttpMethod.GET, "/posts/user").authenticated()
                        // Protected endpoints
                        .requestMatchers(HttpMethod.GET, "/user/me")
                            .hasAnyAuthority("SCOPE_read:profile")
                        .requestMatchers(HttpMethod.POST, "/user/register")
                            .hasAnyAuthority("SCOPE_write:profile")
                        .requestMatchers(HttpMethod.POST, "/posts/create")
                            .hasAnyAuthority("SCOPE_write:posts")
                        .requestMatchers(HttpMethod.GET, "/user/users/**")
                            .hasAnyAuthority("SCOPE_read:profile")
                        // The rest has to be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = JwtDecoders.fromOidcIssuerLocation(
                "https://" + domain + "/"
        );

        OAuth2TokenValidator<Jwt> audienceValidator = token ->
                token.getAudience().contains(audience)
                        ? OAuth2TokenValidatorResult.success()
                        : OAuth2TokenValidatorResult.failure(
                        new OAuth2Error("invalid_token", "Invalid audience", null)
                );

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(
                "https://" + domain + "/"
        );

        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(
                withIssuer, audienceValidator
        );

        decoder.setJwtValidator(withAudience);
        return decoder;
    }
}