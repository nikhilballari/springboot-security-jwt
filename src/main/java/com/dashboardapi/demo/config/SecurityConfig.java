package com.dashboardapi.demo.config;

import com.dashboardapi.demo.filter.JwtAuthFilter;
import com.dashboardapi.demo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    /**
     * Method implementation for Spring Security Authentication using UserDetailsService
     * @return UserDetailsService instance
     */
    @Bean
    public UserDetailsService customUserDetailsService() {
        return new MyUserDetailsService();
    }

    /**
     * Method implementation for Spring Security Authorization using HttpSecurity
     * @return UserDetailsService instance
     */

    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        /*
                This code ignores the CSRF for specified endpoints
        return httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/dashboards/*"))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/dashboards/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/dashboards").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/dashboards").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/dashboards").permitAll()
                        .anyRequest().denyAll())
                .formLogin(Customizer.withDefaults())
                .build();
        */

        return httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/dashboards/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users/**","/dashboards","/authenticate").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/dashboards/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/dashboards/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/management-dashboard/**").permitAll()
                        .anyRequest().permitAll())
//                .formLogin(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

        /*
                This code disables the CSRF protection which ON by default
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/dashboards/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/dashboards").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/dashboards").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/dashboards").permitAll()
                        .requestMatchers(HttpMethod.GET,"/management-dashboard/**").permitAll()
                        .anyRequest().denyAll())
                .formLogin(Customizer.withDefaults())
                .build();
        */
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
