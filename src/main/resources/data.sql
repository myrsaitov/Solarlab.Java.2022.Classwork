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
INSERT INTO users (username, email, enabled, password) VALUES ('admin', null, true, '$2a$10$9OJhEIj3eZo3A7pTbPJC8e6cExaFTs3EOVjwLUS9JK5TXPCtBUx2e');
INSERT INTO users (username, email, enabled, password) VALUES ('user1', null, true, '$2a$10$9OJhEIj3eZo3A7pTbPJC8e6cExaFTs3EOVjwLUS9JK5TXPCtBUx2e');
INSERT INTO users (username, email, enabled, password) VALUES ('user2', null, true, '$2a$10$9OJhEIj3eZo3A7pTbPJC8e6cExaFTs3EOVjwLUS9JK5TXPCtBUx2e');


-- authorities
INSERT INTO task.authorities (id, authority, username) VALUES (1, 'ADMIN', 'admin');
INSERT INTO task.authorities (id, authority, username) VALUES (2, 'USER', 'user1');
INSERT INTO task.authorities (id, authority, username) VALUES (3, 'USER', 'user2');
