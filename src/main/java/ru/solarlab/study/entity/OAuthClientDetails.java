package ru.solarlab.study.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** Модель клиента сервера авторизации в БД */
@Entity
@Table(name = "oauth_client_details")
@NoArgsConstructor
@Getter
@Setter
public class OAuthClientDetails {

    /** Идентификатор клиента (Название сервиса) */
    @Id
    @Column(name = "client_id")
    private String clientId;

    /** Идентификатора сервера ресурсов */
    @Column(name = "resource_ids")
    private String resourceIds;

    /** Секретный ключ клиента */
    @Column(name = "client_secret")
    private String clientSecret;

    /** Гранты oauth, доступные пользователю */
    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    /** Страница редиректа */
    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    /** Время действия токена */
    @Column(name = "access_token_validity")
    private int accessTokenValidity;

    /** Время действия refresh токена */
    @Column(name = "refresh_token_validity")
    private int refreshTokenValidity;

    /** Дополнительная информация */
    @Column(name = "additional_information")
    private String additionalInformation;

    /** Область действия токена */
    private String scope;

    /** Полномочия */
    private String authorities;

    private boolean autoapprove;
}

