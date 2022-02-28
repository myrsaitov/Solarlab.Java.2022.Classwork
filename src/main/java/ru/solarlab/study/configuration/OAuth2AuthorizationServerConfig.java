package ru.solarlab.study.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Конфигурация встроенного сервиса авторизации
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /** Шифратор паролей пользователей */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /** Менеджер аутентификации */
    @Autowired
    private AuthenticationManager authenticationManager;

    /** JWT конвертер oauth токенов */
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;


    /** Конфигурация модулей auth сервиса */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(new JwtTokenStore(jwtAccessTokenConverter))
                .accessTokenConverter(jwtAccessTokenConverter)
                .authenticationManager(authenticationManager);
    }

    /** Конфигурация security auth сервиса */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    /** Конфигурация in-memory клиентов auth сервиса. TODO: Попробовать вынести хранение в БД + хардкод в sql скрипты */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("task-client").secret(passwordEncoder.encode("123456789"))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("MAIN_CLIENT")
                .scopes("task_info")
                .resourceIds("oauth2-resource")
                .redirectUris("http://localhost:8081/login")
                .accessTokenValiditySeconds(120)
                .refreshTokenValiditySeconds(240000);
    }
}