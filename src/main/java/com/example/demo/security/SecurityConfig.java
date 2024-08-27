package com.example.demo.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.service.UserService;

@Configuration
public class SecurityConfig {
@Autowired
private UserService userService;

@Bean
public static PasswordEncoder passwordEncoder() {
return new BCryptPasswordEncoder();
}

@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
return configuration.getAuthenticationManager();
}

@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http.authorizeRequests().requestMatchers("/registration**").permitAll()
         .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
         .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll();
             http.csrf().disable();
            http.headers().frameOptions().disable();
return http.build();
}
    
@Bean
public DaoAuthenticationProvider authenticationProvider() {
DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
auth.setUserDetailsPasswordService((UserDetailsPasswordService) userService);
auth.setPasswordEncoder(passwordEncoder());
return auth;
     }
}