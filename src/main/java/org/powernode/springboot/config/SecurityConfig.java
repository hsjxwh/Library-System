package org.powernode.springboot.config;

import org.powernode.springboot.fliter.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${open.api}")
    String[] openApi;
    @Value("${login.api}")
    String[] loginApi;
    @Value("${manager.api}")
    String[] managerApi;
    JwtFilter jwtFilter;
    SecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter=jwtFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //无状态会话
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //禁用csrf防御，目前已有
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests ->authorizeRequests
                        .requestMatchers(openApi).permitAll()
                        .requestMatchers(loginApi).hasAnyRole("user","manager")
                        .requestMatchers(managerApi).hasRole("manager")
                        .anyRequest().permitAll())
                        ;
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        //创建存储cors配置信息的对象
        CorsConfiguration config = new CorsConfiguration();
        //配置允许访问的源
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:8081","https://whswlibrarysystem.top","https://www.whswlibrarysystem.top"));
        //允许携带凭证
        config.setAllowCredentials(true);
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("X-CSRF-TOKEN", "Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("X-CSRF-TOKEN"));
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //将配置应用到所有的url当中
        source.registerCorsConfiguration("/**",config);
        return source;
    }

}
