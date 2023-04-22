package com.linkyB.backend.common.config.security;

import com.linkyB.backend.common.config.security.filter.CustomExceptionHandleFilter;
import com.linkyB.backend.common.config.security.filter.CustomUsernamePasswordAuthenticationFilter;
import com.linkyB.backend.common.config.security.filter.JwtAuthenticationFilter;
import com.linkyB.backend.common.config.security.filter.ReissueAuthenticationFilter;
import com.linkyB.backend.common.config.security.handler.CustomAccessDeniedHandler;
import com.linkyB.backend.common.config.security.handler.CustomAuthenticationEntryPoint;
import com.linkyB.backend.common.config.security.handler.CustomAuthenticationFailureHandler;
import com.linkyB.backend.common.config.security.handler.CustomAuthenticationSuccessHandler;
import com.linkyB.backend.common.config.security.provider.JwtAuthenticationProvider;
import com.linkyB.backend.common.config.security.provider.ReissueAuthenticationProvider;
import com.linkyB.backend.common.result.ResultCode;
import com.linkyB.backend.common.util.JwtUtil;
import com.linkyB.backend.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

import static com.linkyB.backend.user.domain.enums.Authority.USER;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtUtil jwtUtil;

    // handlers
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    // providers
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final ReissueAuthenticationProvider reissueAuthenticationProvider;

    // filter
    private final CustomExceptionHandleFilter customExceptionHandleFilter;


    private static final String[] AUTH_WHITELIST_STATIC = {"/static/css/**", "/static/js/**", "*.ico"};
    private static final String[] AUTH_WHITELIST_SWAGGER = {
            "/v3/api-docs", "/configuration/ui", "/swagger-resources/**",
            "/configuration/security", "/webjars/**", "/swagger/**",
            "/swagger-ui/**", "/swagger-ui/index.html"
    };
    private static final String[] AUTH_WHITELIST = {
            "/auth/login", "/auth/reissue",
            "/auth/signup", "/auth/signup/confirm-email", "/auth/signup/send-email",
            "/auth/check-nickname", "/auth/profile-images",
            "/auth/reset-password", "/auth/reset-password/send-email",
            "/chat/**", "/ws-stomp"

    };

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);

        return daoAuthenticationProvider;
    }

    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        final CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        return filter;
    }

    JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        final List<String> skipPaths = new ArrayList<>();
        skipPaths.addAll(Arrays.asList(AUTH_WHITELIST));
        skipPaths.addAll(Arrays.asList(AUTH_WHITELIST_STATIC));
        skipPaths.addAll(Arrays.asList(AUTH_WHITELIST_SWAGGER));

        final RequestMatcher matcher = new CustomRequestMatcher(skipPaths);
        final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(matcher, jwtUtil);
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

    ReissueAuthenticationFilter reissueAuthenticationFilter(AuthenticationManager authenticationManager) {
        final ReissueAuthenticationFilter filter = new ReissueAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        return filter;
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    private void configureCustomBeans() {
        final Map<String, ResultCode> map = new HashMap<>();
        map.put("/auth/login", ResultCode.LOGIN_SUCCESS);
        map.put("/auth/reissue", ResultCode.REISSUE_SUCCESS);
        customAuthenticationSuccessHandler.setResultCodeMap(map);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // below order is very important
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(reissueAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider)
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        configureCustomBeans();

        // http config
        http.exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.logout().disable()
                .formLogin().disable()
                .httpBasic().disable();

        http.cors()
                .configurationSource(configurationSource())
                .and()
                .csrf()
                .disable()
                .authenticationManager(authenticationManager)
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest)
                .permitAll()
                .antMatchers(AUTH_WHITELIST_STATIC)
                .permitAll()
                .antMatchers(AUTH_WHITELIST_SWAGGER)
                .permitAll()
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                .anyRequest().hasAuthority(USER.toString());

        http.addFilterBefore(customUsernamePasswordAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(authenticationManager), CustomUsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(reissueAuthenticationFilter(authenticationManager), JwtAuthenticationFilter.class);
        http.addFilterBefore(customExceptionHandleFilter, ReissueAuthenticationFilter.class);

        return http.build();
    }
}