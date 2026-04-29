package at.codersbay.courseapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // Public: user
                .antMatchers(HttpMethod.POST, "/api/user").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/user").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/user/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/user").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/api/user").hasAnyRole("ADMIN", "USER")

                .antMatchers(HttpMethod.POST, "/api/course").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/api/course").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/course/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/course").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/api/course").hasAnyRole("ADMIN", "USER")

                .antMatchers(HttpMethod.PATCH, "/api/course").hasAnyRole("ADMIN", "USER")

                .anyRequest().authenticated()
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
