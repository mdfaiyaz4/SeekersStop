package com.faiyaz.SeekersStop.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringConfig {

    public SpringConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.csrf(csrf -> csrf.disable())
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
                        .requestMatchers(HttpMethod.GET, "/applications/*")
                        .hasRole("JOB_SEEKER")
                        .requestMatchers(HttpMethod.PATCH, "/applications/*/status")
                        .hasRole("RECRUITER")

                        .requestMatchers(HttpMethod.POST ,"/jobs")
                        .hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.PUT ,"/jobs/*")
                        .hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.GET ,"/jobs")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET,"/jobs/{id}")
                        .authenticated()
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
                        .anyRequest()
                        .authenticated());
                        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class );
        return http.build();


    }

}
