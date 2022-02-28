package ru.solarlab.study.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Глобальная конфигурация безопасности endpoint`ов приложения
 */
@Configuration
/** Если возникают проблемы в отладке безопасности, то можно прокинуть в аннотацию EnableWebSecurity параметр debug = true, для подробного логирования метаданных по входящим запросам и фильтрам */
@EnableWebSecurity(debug = false)
/** EnableGlobalMethodSecurity обязательно необходит для работы аннотации PreAuhotrize */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GlobalSecurityConfig extends WebSecurityConfigurerAdapter {

    /** Ключ для симметричного шифрования JWT токена */
    @Value("${secure.signingKey}")
    private String signingKey;

    /** Менеджер аутентификации */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /** In-memory сервис пользователей. TODO: Попробовать вынести хранение в БД + хардкод в sql скрипты */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        // администратор
        manager.createUser(
                User
                        .withUsername("admin")
                        .password(passwordEncoder().encode("password"))
                        .roles("ADMIN")
                        .authorities("ADMIN")
                        .build());
        // пользователь 1
        manager.createUser(
                User
                        .withUsername("user1")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER")
                        .authorities("USER")
                        .build());
        // пользователь 2
        manager.createUser(
                User
                        .withUsername("user2")
                        .password(passwordEncoder().encode("password"))
                        .roles("USER")
                        .authorities("USER")
                        .build());
        return manager;
    }

    /** Глобальная конфигурация защиты endpoint`ов */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/v3/api-docs.yaml", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .antMatchers("/oauth/**", "/login**", "/error**").permitAll()
                .anyRequest().authenticated();

    }

    /** Конфигурация менеджера по аутентификации */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    /** Бин для шифрования паролей пользователей */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Кастомный сервис по работе с oauth токенами */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);

        return defaultTokenServices;
    }

    /** Кастомное хранилище oauth токенов */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /** Кастомный конвертер oauth токенов */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        converter.setAccessTokenConverter(accessTokenConverter);
        converter.setSigningKey(signingKey);
        return converter;
    }
}