package todo.list.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import todo.list.security.entrypoint.TokenAuthenticationEntryPoint;
import todo.list.security.filter.LoginFilter;
import todo.list.security.handler.JwtAccessDeninedHandler;
import todo.list.token.TokenProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAccessDeninedHandler jwtAccessDeninedHandler;
    private final TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(Customizer.withDefaults())
            .addFilterBefore(new LoginFilter(tokenProvider, tokenAuthenticationEntryPoint),
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling((exceptionConfig) -> exceptionConfig.authenticationEntryPoint(
                tokenAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeninedHandler)
            )
            .authorizeHttpRequests(authorizeRequest ->
                authorizeRequest
                    .anyRequest().permitAll()
            )
            .headers(
                headersConfigurer ->
                    headersConfigurer
                        .frameOptions(
                            HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
            );

        return http.build();
    }

}
