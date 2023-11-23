INSERT INTO users (id, email, password, first_name, last_name)
VALUES (1, 'bob@test.app', '$2a$10$eNDxAZLdjj7A42', 'Bob', 'Smith'),
       (2, 'alice@test.app', '$2a$10$eNDxAZLdjj7A42', 'Alice', 'Smith'),
       (3, 'john@test.app', '$2a$10$eNDxAZLdjj7A42', 'John', 'Lea'),
       (4, 'harry@test.app', '$2a$10$eNDxAZLdjj7A42', 'Harry', 'Potter');
ALTER SEQUENCE users_id_seq RESTART WITH 5;

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 1);
