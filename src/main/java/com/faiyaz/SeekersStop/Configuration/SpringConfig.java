package com.faiyaz.SeekersStop.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SpringConfig {

    public SpringConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    private final JwtFilter jwtFilter;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of(HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**"
                                ).permitAll()
                                .requestMatchers("/auth/register","/auth/login")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/applications")
                                .hasRole("JOB_SEEKER")
                                .requestMatchers(HttpMethod.GET, "/applications/recruiter")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.GET, "/applications/my")
                                .hasRole("JOB_SEEKER")
                                .requestMatchers(HttpMethod.GET, "/applications/*/resume")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.GET, "/applications/*")
                                .hasRole("JOB_SEEKER")
                                .requestMatchers(HttpMethod.PATCH, "/applications/*/status")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.PATCH, "/applications/*/withdraw")
                                .hasRole("JOB_SEEKER")

                                .requestMatchers(HttpMethod.POST ,"/jobs")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.PUT ,"/jobs/*")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.GET ,"/jobs")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"/jobs/my")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.GET,"/jobs/{id}")
                                .permitAll()
                                .requestMatchers(HttpMethod.DELETE,"/jobs/deactive/{id}")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.PUT,"/jobs/active/{id}")
                                .hasRole("RECRUITER")



                                .requestMatchers(HttpMethod.POST, "/recruiter/profile")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.GET, "/recruiter/profile")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.PUT, "/recruiter/profile")
                                .hasRole("RECRUITER")

                                .requestMatchers(HttpMethod.POST, "/company")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.GET, "/company")
                                .hasRole("RECRUITER")
                                .requestMatchers(HttpMethod.PUT, "/company")
                                .hasRole("RECRUITER")

                                .requestMatchers(HttpMethod.POST, "/jobseeker/profile")
                                .hasRole("JOB_SEEKER")
                                .requestMatchers(HttpMethod.PUT, "/jobseeker/profile")
                                .hasRole("JOB_SEEKER")
                                .requestMatchers(HttpMethod.GET, "/jobseeker/profile")
                                .hasRole("JOB_SEEKER")
                                .requestMatchers(HttpMethod.GET, "/jobseeker/cv")
                                .hasRole("JOB_SEEKER")
                                .requestMatchers(HttpMethod.PUT, "/jobseeker/cv")
                                .hasRole("JOB_SEEKER")
                                .anyRequest()
                                .authenticated());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class );
        return http.build();


    }

}
