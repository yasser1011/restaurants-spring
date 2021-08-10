package com.example.backend.security;

import com.example.backend.filter.CustomAuthenticationFilter;
import com.example.backend.filter.CustomAuthorizationFilter;
import com.example.backend.filter.CustomCorsFilter;
import com.example.backend.service.UserService;
import com.example.backend.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@CrossOrigin
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, UserService userService, JWTUtil jwtUtil, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry
//                        .addMapping("/**")
//                        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
//            }
//        };
//    }


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST","PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("Access-Control-Allow-Origin")
////                .exposedHeaders("Access-Control-Allow-Origin")
//                .allowCredentials(false).maxAge(3600);
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.headers().addHeaderWriter(
                new StaticHeadersWriter("Access-Control-Allow-Origin", "*"));

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), jwtUtil, userService);
        customAuthenticationFilter.setFilterProcessesUrl("/users/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/users/login/**", "/users/register/**",
                "/restaurants", "/").permitAll();
        http.authorizeRequests().regexMatchers( "/restaurants/(\\d+)").permitAll();

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/admin/**").hasAnyAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/restaurants/register/**").hasAnyAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/restaurants/edit/**").hasAnyAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/restaurants/delete/**").hasAnyAuthority("admin");

        http.authorizeRequests().anyRequest().authenticated();

//        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), jwtUtil));
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomCorsFilter(), ChannelProcessingFilter.class);
        http.addFilterBefore(new CustomAuthorizationFilter(userDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource()
//    {
//        final CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("*"));
//        configuration.setAllowedMethods(List.of("HEAD",
//                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setExposedHeaders(List.of("X-Auth-Token","Authorization","Access-Control-Allow-Origin","Access-Control-Allow-Credentials"));
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Override
    @Bean
    // to use authenticationManager everywhere
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
