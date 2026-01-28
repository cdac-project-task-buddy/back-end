package com.taskbuddy.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration // To declare a java config class (equivalent to bean config xml file)
@EnableWebSecurity // to enable spring security
@EnableMethodSecurity // optional to add method level authorization rules
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {
	// ctor based D.I
	private final PasswordEncoder passwordEncoder;
	private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
	//private final DaoAuthenticationProvider authenticationProvider;

	/*
	 * Configure Spring sec filter chain as a spring bean (@Bean) , to override the
	 * spring sec defaults - Disable CSRF protection - Disable HttpSession - Disable
	 * login / logout page generation (i.e disable form login) - retain Basic
	 * Authentication scheme. - Add authorization rules - swagger , sign in , sign
	 * up , listing doctors.. - public end points - any other request - authenticate
	 * Add HttpSecurity as the dependency - to build sec filter chain
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		log.info("********configuring spring sec filter chain*******");
		// disable CSRF protection
		http
	    .csrf(csrf -> csrf.disable())
	    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	    .sessionManagement(session ->
	        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    )
	    .authorizeHttpRequests(auth -> auth

	        // 🔓 AUTH + SWAGGER
	        .requestMatchers(
	            "/auth/**",
	            "/v3/api-docs/**",
	            "/swagger-ui/**"
	        ).permitAll()

	        // 🔓 PUBLIC READ
	        .requestMatchers("/api/auth/**").permitAll()
	        .requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
	        .requestMatchers("/api/workers/**").permitAll()

	        // 🔒 ROLE BASED
	        .requestMatchers("/customers/**").hasAuthority("ROLE_CUSTOMER")
	        .requestMatchers("/workers/**").hasAuthority("ROLE_WORKER")
	        .requestMatchers("/bookings/**")
	            .hasAnyAuthority("ROLE_CUSTOMER", "ROLE_WORKER")

	        // 🔒 EVERYTHING ELSE
	        .anyRequest().authenticated()
	    )
	    .authenticationProvider(authenticationProvider)
	    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
	    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(Arrays.asList("*"));
	    configuration.setAllowCredentials(true);
	    
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
	
	

	    
}
