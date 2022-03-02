TRUNCATE TABLE tasks CASCADE;

INSERT INTO tasks (id, name, started_at, status)
VALUES (1, 'FromMigrationTask', now(), 0);