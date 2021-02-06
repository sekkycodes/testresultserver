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

ALTER TABLE testcases ADD CONSTRAINT fk_testcases_testsuites_id
    FOREIGN KEY (test_suite_id) REFERENCES testsuites(id)