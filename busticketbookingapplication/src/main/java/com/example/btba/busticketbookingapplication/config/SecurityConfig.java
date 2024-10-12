package com.example.btba.busticketbookingapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Telling my application that this is a configuration file to Spring not just security
@EnableWebSecurity // Telling that I don't wanna go to the default Spring Security configuration. I want to configure here.
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(customizer -> customizer.disable())
                // disabling csrf, else we need to pass csrf token for things like post, put, delete requests

                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/images/**").permitAll() // Allow access to images
                        .anyRequest().authenticated()
                )
                // to tell that all the endpoints are protected

                .formLogin( customizer -> customizer
                        .loginPage("/auth/login")
                        // The controller method that renders the html page should have this endpoint of GET type
                        // Further the same html login page should make a POST request to this same url in order to authenticate/login
                        .defaultSuccessUrl("/passenger/home", true)
                        .permitAll()
                )
//                .formLogin(Customizer.withDefaults())
                // utilizes the default login page

                .httpBasic(Customizer.withDefaults())
                // for clients like postman which expect a json response instead of a website when directly giving the credentials

//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // creates a new session id everytime even when refreshed.
                // So, login through a webpage is impossible unless the formlogin line is removed.
                // Clicking login after giving in the username and pass will change the session id.
                // Removing formLogin will create a pop-up that will ask for the credentials.
                // No issues through postman though.

                .logout(out->out
                        .logoutUrl("/logout") // Sets the URL that triggers the logout action.
                        .logoutSuccessUrl("/auth/login?logout") // After logging out, users will be redirected to the login
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
                return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
//        Method 1
//        UserDetails user1 = User.builder()
//                .username("Ash")
//                .password("A@123")
//                .roles("PASSENGER")
//                .build();
//        UserDetails user2 = User.builder()
//                .username("Sky")
//                .password("S@123")
//                .roles("PASSENGER")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
        return new PassengerDetailsService();
    }
//     Methods for defining a UserDetailsService Bean:
//     1. Returning a new InMemoryUserDetailsManager as above
//     2. Returning a custom UserDetailsService class

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider () {
        // On setting this bean, the credentials that we define here will be used.
        // Ones defined at the application.properties will not be overlooked.
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
//        auth.setPasswordEncoder(passwordEncoder());
        // We are not using the password encoder.
        // So, the database should have a prefix {noop} for the password field.
        return auth;
    }
}