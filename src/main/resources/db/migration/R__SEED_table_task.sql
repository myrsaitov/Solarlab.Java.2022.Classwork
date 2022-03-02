TRUNCATE TABLE task CASCADE;

INSERT INTO task (id, name, started_at, status)
VALUES (1, 'FromMigrationTask', now(), 0);