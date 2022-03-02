create table "users"
(
    id                uuid    not null
        constraint user_id_pkey
            primary key,
    email             varchar(255),
    enabled           boolean not null,
    password          varchar(255),
    username          varchar(255)
        constraint user_username_key
            unique
);

create table authorities (
                             username varchar(50) not null,
                             authority varchar(50) not null,
                             constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

create table oauth_client_details
(
    client_id               varchar(255) not null
        constraint oauth_client_details_client_id_pkey
            primary key,
    access_token_validity   integer,
    additional_information  varchar(255),
    authorities             varchar(255),
    authorized_grant_types  varchar(255),
    autoapprove             boolean      not null,
    client_secret           varchar(255),
    refresh_token_validity  integer,
    resource_ids            varchar(255),
    scope                   varchar(255),
    web_server_redirect_uri varchar(255)
);


-- task-client
INSERT INTO oauth_client_details (client_id,
                                  client_secret,
                                  authorized_grant_types,
                                  scope,
                                  web_server_redirect_uri,
                                  authorities,
                                  access_token_validity,
                                  refresh_token_validity,
                                  additional_information,
                                  autoapprove)
VALUES ('task-client', -- client-id
        '$2a$10$ftGBqfb6LCFoQTbGR8OAf.QGV7e97jwLhboX8OB0OIQzuFH8F5cKa', -- hash client secret: 3vKDcTh5C4
        'password,refresh_token', -- grant types
        'tasks', -- scope
        null,
        null,
        3600,
        36000,
        null,
        true);

-- users, hash of password: password

INSERT INTO users (id, username, email, enabled, password) VALUES ('277a8158-fe9f-4c2b-9803-ee7d23927c71', 'admin', null, true, '$2a$10$9OJhEIj3eZo3A7pTbPJC8e6cExaFTs3EOVjwLUS9JK5TXPCtBUx2e');
INSERT INTO users (id, username, email, enabled, password) VALUES ('277a8158-fe9f-4c2b-9803-ee7d23927c72', 'user1', null, true, '$2a$10$9OJhEIj3eZo3A7pTbPJC8e6cExaFTs3EOVjwLUS9JK5TXPCtBUx2e');
INSERT INTO users (id, username, email, enabled, password) VALUES ('277a8158-fe9f-4c2b-9803-ee7d23927c73', 'user2', null, true, '$2a$10$9OJhEIj3eZo3A7pTbPJC8e6cExaFTs3EOVjwLUS9JK5TXPCtBUx2e');


-- authorities
INSERT INTO authorities (authority, username) VALUES ('ADMIN', 'admin');
INSERT INTO authorities (authority, username) VALUES ('USER', 'user1');
INSERT INTO authorities (authority, username) VALUES ('USER', 'user2');
