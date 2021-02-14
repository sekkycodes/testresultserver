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
