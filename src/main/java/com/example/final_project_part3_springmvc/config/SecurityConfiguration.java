package com.example.final_project_part3_springmvc.config;

import com.example.final_project_part3_springmvc.exception.NotFoundException;
import com.example.final_project_part3_springmvc.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final PersonService personService;
    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfiguration(PersonService personService, BCryptPasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
//                        s -> s.requestMatchers(HttpMethod.GET, "/person").hasAnyRole("USER", "ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/person").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.POST).permitAll()).httpBasic(Customizer.withDefaults());
                s -> s.anyRequest().permitAll()).httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Autowired
    public void configureBuild(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(username -> (UserDetails) personService
                        .findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("chom")))
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); } }
