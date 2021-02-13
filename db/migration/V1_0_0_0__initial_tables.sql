CREATE TABLE testsuites (
    id CHAR(36),
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE testcases(
    id CHAR(36),
    test_suite_id CHAR(36),
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE testsuite_executions (
    id_name VARCHAR(255),
    id_time BIGINT,
    duration BIGINT,
    PRIMARY KEY (id_name, id_time)
);

CREATE TABLE testcase_executions (
    id_name VARCHAR(255),
    id_time BIGINT,
    suite_name VARCHAR(255),
    result VARCHAR(10),
    duration BIGINT,
    PRIMARY KEY (id_name, id_time)
);

ALTER TABLE testcases ADD CONSTRAINT fk_testcases_testsuites_id
    FOREIGN KEY (test_suite_id) REFERENCES testsuites(id);